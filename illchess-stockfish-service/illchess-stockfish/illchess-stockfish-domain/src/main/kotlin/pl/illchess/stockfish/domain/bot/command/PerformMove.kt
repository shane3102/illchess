package pl.illchess.stockfish.domain.bot.command

import pl.illchess.stockfish.domain.bot.domain.Bot

data class PerformMove(
    val bot: Bot,
    val move: String
)
