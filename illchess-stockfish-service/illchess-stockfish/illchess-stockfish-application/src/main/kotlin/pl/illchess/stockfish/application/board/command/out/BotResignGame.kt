package pl.illchess.stockfish.application.board.command.out

import pl.illchess.stockfish.domain.bot.domain.Bot

interface BotResignGame {
    fun botResignGame(bot: Bot)
}