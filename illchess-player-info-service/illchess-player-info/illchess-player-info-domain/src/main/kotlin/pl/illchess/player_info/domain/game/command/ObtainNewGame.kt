package pl.illchess.player_info.domain.game.command

import pl.illchess.player_info.domain.game.model.EndTime
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.GameResult
import pl.illchess.player_info.domain.game.model.PerformedMove
import pl.illchess.player_info.domain.user.model.User

data class ObtainNewGame(
    val gameId: GameId,
    val whiteUser: User,
    val blackUser: User,
    val gameResult: GameResult,
    val endTime: EndTime,
    val performedMoves: List<PerformedMove>
)
