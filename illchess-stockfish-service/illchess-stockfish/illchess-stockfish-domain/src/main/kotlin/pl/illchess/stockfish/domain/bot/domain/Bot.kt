package pl.illchess.stockfish.domain.bot.domain

import java.time.LocalDateTime
import pl.illchess.stockfish.domain.board.domain.BoardId

data class Bot(
    val username: Username,
    val expirationDate: LocalDateTime,
    var currentBoardId: BoardId?,
    val obtainedBestMovesCount: Int,
    val searchedDepth: Int
)

