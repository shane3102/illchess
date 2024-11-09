package pl.illchess.player_info.adapter.game.query.out.jpa_streamer

import com.speedment.jpastreamer.application.JPAStreamer
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.game.query.out.jpa_streamer.mapper.GameViewMapper
import pl.illchess.player_info.adapter.shared_entities.GameEntity
import pl.illchess.player_info.adapter.shared_entities.GameEntityMetaModel
import pl.illchess.player_info.application.commons.query.model.Page
import pl.illchess.player_info.application.game.query.out.GameSnippetViewLatestQueryPort
import pl.illchess.player_info.application.game.query.out.GameViewQueryPort
import pl.illchess.player_info.application.game.query.out.model.GameSnippetView
import pl.illchess.player_info.application.game.query.out.model.GameView
import java.util.UUID

@ApplicationScoped
class GameViewJpaStreamerRepository(
    private val jpaStreamer: JPAStreamer
) : GameViewQueryPort, GameSnippetViewLatestQueryPort {

    override fun findById(id: UUID): GameView? {

        val sc = StreamConfiguration.of(GameEntity::class.java)
            .joining(GameEntityMetaModel.whiteUser)
            .joining(GameEntityMetaModel.blackUser)

        return jpaStreamer.stream(sc)
            .filter { it.id == id }.findFirst()
            .map { GameViewMapper.toView(it) }
            .orElse(null)
    }

    override fun findLatestGamesPageable(pageNumber: Int, pageSize: Int): Page<GameSnippetView> {
        val sc = StreamConfiguration.of(GameEntity::class.java)
            .joining(GameEntityMetaModel.whiteUser)
            .joining(GameEntityMetaModel.blackUser)

        val streamSupplier = { jpaStreamer.stream(sc) }

        val count = streamSupplier.invoke().count()
        val totalPages = (count / pageSize) - (if (count % pageSize == 0L) 1 else 0)

        val content = streamSupplier.invoke()
            .sorted(GameEntityMetaModel.endTime.reversed())
            .skip(pageNumber.toLong() * pageSize.toLong())
            .limit(pageSize.toLong())
            .map { GameViewMapper.toSnippetView(it) }
            .toList()

        return Page(content, pageNumber, pageSize, totalPages.toInt())
    }
}