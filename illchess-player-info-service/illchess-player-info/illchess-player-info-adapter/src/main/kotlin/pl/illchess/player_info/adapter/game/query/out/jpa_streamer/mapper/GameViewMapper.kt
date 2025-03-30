package pl.illchess.player_info.adapter.game.query.out.jpa_streamer.mapper

import pl.illchess.player_info.adapter.shared_entities.GameEntity
import pl.illchess.player_info.application.game.query.out.model.GameSnippetView
import pl.illchess.player_info.application.game.query.out.model.GameView

class GameViewMapper {
    companion object {
        fun toView(gameEntity: GameEntity) = GameView(
            gameEntity.id,
            GameView.UserGameInfoView(
                gameEntity.whiteUser.username,
                gameEntity.whiteRankingPointsBeforeGame,
                gameEntity.whiteRankingPointsAfterGame,
                gameEntity.whiteRankingPointsChange
            ),
            GameView.UserGameInfoView(
                gameEntity.blackUser.username,
                gameEntity.blackRankingPointsBeforeGame,
                gameEntity.blackRankingPointsAfterGame,
                gameEntity.blackRankingPointsChange
            ),
            gameEntity.gameResult,
            gameEntity.gameResultCause,
            gameEntity.endTime,
            gameEntity.performedMoves.map { moveEntity ->
                GameView.PerformedMoveView(
                    moveEntity.startSquare,
                    moveEntity.endSquare,
                    moveEntity.stringValue,
                    moveEntity.color
                )
            }
        )

        fun toSnippetView(gameEntity: GameEntity) = GameSnippetView(
            gameEntity.id,
            gameEntity.whiteUser.username,
            gameEntity.whiteRankingPointsChange,
            gameEntity.blackUser.username,
            gameEntity.blackRankingPointsChange,
            gameEntity.gameResult,
            gameEntity.endTime
        )
    }
}