package pl.illchess.stockfish.domain.bot.domain

import java.time.LocalDateTime
import pl.illchess.stockfish.domain.board.domain.GameId

data class Bot(
    val username: Username,
    val expirationDate: LocalDateTime,
    var currentGameId: GameId?,
    val obtainedBestMovesCount: Int,
    val searchedDepth: Int
)

