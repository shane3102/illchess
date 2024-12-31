package pl.illchess.stockfish.domain.board.domain

import pl.illchess.stockfish.domain.bot.domain.Username

data class BoardAdditionalInfo(
    val boardId: BoardId,
    val currentPlayerColor: String,
    val whitePlayerUsername: Username,
    val blackPlayerUsername: Username?,
    val gameState: String,
    val victoriousPlayerColor: String?
)
