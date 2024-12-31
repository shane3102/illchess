package pl.illchess.stockfish.application.bot.command.out

import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username

interface LoadBot {
    fun loadBot(username: Username): Bot?
}