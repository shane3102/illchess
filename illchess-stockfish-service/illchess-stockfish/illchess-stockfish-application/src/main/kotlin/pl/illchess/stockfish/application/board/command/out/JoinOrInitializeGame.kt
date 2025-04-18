package pl.illchess.stockfish.application.board.command.out

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.bot.domain.Username

interface JoinOrInitializeGame {
    fun joinOrInitialize(username: Username): GameId?
}