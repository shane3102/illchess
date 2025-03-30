package pl.illchess.player_info.application.game.query.out.model

import java.time.LocalDateTime
import java.util.UUID
import pl.illchess.player_info.domain.game.model.GameResultCause

data class GameView(
    val id: UUID,
    val whiteUserGameInfo: UserGameInfoView,
    val blackUserGameInfo: UserGameInfoView,
    val gameResult: String,
    val gameResultCause: String,
    val endTime: LocalDateTime,
    val performedMoves: List<PerformedMoveView>
) {
    data class UserGameInfoView(
        val username: String,
        val rankingPointsBeforeGame: Int,
        val rankingPointsAfterGame: Int,
        val rankingPointsChange: Int
    )

    data class PerformedMoveView(
        val startSquare: String,
        val endSquare: String,
        val stringValue: String,
        val color: String
    )
}
