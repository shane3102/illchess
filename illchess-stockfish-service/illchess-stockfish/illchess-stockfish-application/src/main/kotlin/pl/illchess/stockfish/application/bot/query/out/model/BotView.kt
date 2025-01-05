package pl.illchess.stockfish.application.bot.query.out.model

import java.util.UUID

data class BotView(
    val username: String,
    val currentBoardId: UUID?
)