package pl.illchess.stockfish.domain.bot.domain

import pl.illchess.stockfish.domain.board.domain.BoardId

data class Bot(
    val username: Username,
    var currentBoardId: BoardId?,
    val obtainedBestMovesCount: Int,
    val searchedDepth: Int
)

