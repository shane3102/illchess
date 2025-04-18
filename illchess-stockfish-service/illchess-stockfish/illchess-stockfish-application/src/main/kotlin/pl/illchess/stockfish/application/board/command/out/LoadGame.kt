package pl.illchess.stockfish.application.board.command.out

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

interface LoadGame {
    fun loadGame(gameId: GameId): FenBoardPosition?
}