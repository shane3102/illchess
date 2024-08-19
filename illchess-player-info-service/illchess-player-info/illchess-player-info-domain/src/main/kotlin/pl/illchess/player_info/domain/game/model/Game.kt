package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.user.model.User

data class Game(
    val id: GameId,
    val whitePlayer: User,
    val blackPlayer: User,
    val winningPieceColor: PieceColor,
    val performedMoves: List<PerformedMove>
)