package pl.illchess.player_info.adapter.game.query.out.jpa_streamer

import com.speedment.jpastreamer.application.JPAStreamer
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.shared_entities.GameEntity
import pl.illchess.player_info.adapter.shared_entities.GameEntityMetaModel
import pl.illchess.player_info.application.game.query.out.GameViewQueryPort
import pl.illchess.player_info.application.game.query.out.model.GameView
import pl.illchess.player_info.application.game.query.out.model.GameView.UserGameInfoView
import java.util.UUID

@ApplicationScoped
class GameViewJpaStreamerRepository(private val jpaStreamer: JPAStreamer) : GameViewQueryPort {

    override fun findById(id: UUID): GameView? {

        val sc = StreamConfiguration.of(GameEntity::class.java)
            .joining(GameEntityMetaModel.whiteUser)
            .joining(GameEntityMetaModel.blackUser)

        return jpaStreamer.stream(sc)
            .filter { it.id == id }.findFirst()
            .map {
                GameView(
                    it.id,
                    UserGameInfoView(
                        it.whiteUser.username,
                        it.whiteRankingPointsBeforeGame,
                        it.whiteRankingPointsAfterGame,
                        it.whiteRankingPointsChange
                    ),
                    UserGameInfoView(
                        it.blackUser.username,
                        it.blackRankingPointsBeforeGame,
                        it.blackRankingPointsAfterGame,
                        it.blackRankingPointsChange
                    ),
                    it.winningPieceColor,
                    it.endTime,
                    it.performedMoves.map { moveEntity ->
                        GameView.PerformedMoveView(
                            moveEntity.startSquare,
                            moveEntity.endSquare,
                            moveEntity.stringValue,
                            moveEntity.color
                        )
                    }
                )
            }
            .orElse(null)
    }
}