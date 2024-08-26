package pl.illchess.player_info.domain.game.command

import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.PerformedMove
import pl.illchess.player_info.domain.game.model.PieceColor
import pl.illchess.player_info.domain.user.model.User

data class ObtainNewGame(
    val gameId: GameId,
    val whiteUser: User,
    val blackUser: User,
    val winningPieceColor: PieceColor,
    val performedMoves: List<PerformedMove>
)
