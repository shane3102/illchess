package pl.illchess.stockfish.application.board.command.out

import pl.illchess.stockfish.domain.bot.command.PerformMove

interface BotPerformMove {
    fun performMove(command: PerformMove)
}