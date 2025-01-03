package pl.illchess.stockfish.application.bot.command.out

import pl.illchess.stockfish.domain.bot.domain.Bot

interface SaveBot {
    fun saveBot(bot: Bot)
}