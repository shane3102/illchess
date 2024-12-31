package pl.illchess.stockfish.application.bot.command.out

import pl.illchess.stockfish.domain.bot.domain.Username

interface DeleteBot {
    fun deleteBot(username: Username)
}