package pl.illchess.stockfish.application.board.command.out

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.bot.domain.Username

interface JoinOrInitializeBoard {
    fun joinOrInitialize(username: Username): BoardId?
}