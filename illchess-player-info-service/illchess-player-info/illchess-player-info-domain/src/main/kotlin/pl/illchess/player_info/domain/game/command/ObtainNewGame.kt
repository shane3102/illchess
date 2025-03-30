package pl.illchess.player_info.domain.game.command

import pl.illchess.player_info.domain.game.model.EndTime
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.GameResult
import pl.illchess.player_info.domain.game.model.GameResultCause
import pl.illchess.player_info.domain.game.model.PerformedMove
import pl.illchess.player_info.domain.player.model.Player

data class ObtainNewGame(
    val gameId: GameId,
    val whitePlayer: Player,
    val blackPlayer: Player,
    val gameResult: GameResult,
    val gameResultCause: GameResultCause,
    val endTime: EndTime,
    val performedMoves: List<PerformedMove>
)
