package pl.illchess.stockfish.application.board.command.out

import pl.illchess.stockfish.domain.bot.domain.Bot

interface BotQuitNotYetStartedGame {
    fun quitNotYetStartedGame(bot: Bot)
}