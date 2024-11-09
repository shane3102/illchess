package pl.illchess.player_info.adapter.player.query.out.jpa_streamer

import com.speedment.jpastreamer.application.JPAStreamer
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.shared_entities.PlayerEntity
import pl.illchess.player_info.adapter.shared_entities.PlayerEntityMetaModel
import pl.illchess.player_info.application.commons.query.model.Page
import pl.illchess.player_info.application.player.query.out.PlayerViewQueryPort
import pl.illchess.player_info.application.player.query.out.model.PlayerView

@ApplicationScoped
class PlayerViewJpaStreamerAdapter(private val jpaStreamer: JPAStreamer) : PlayerViewQueryPort {

    override fun findHighestRatedPlayersPageable(pageNumber: Int, pageSize: Int): Page<PlayerView> {
        val streamSupplier = { jpaStreamer.stream(PlayerEntity::class.java) }

        val count = streamSupplier.invoke().count()
        val totalPages = streamSupplier.invoke().count() / pageSize - (if (count % pageSize == 0L) 1 else 0)

        val content = streamSupplier.invoke()
            .sorted(PlayerEntityMetaModel.currentRankingPoints.reversed())
            .skip(pageNumber.toLong() * pageSize.toLong())
            .limit(pageSize.toLong())
            .map { PlayerView(it.id, it.username, it.currentRankingPoints) }
            .toList()
        val result = Page(content, pageNumber, pageSize, totalPages.toInt())
        return result
    }
}