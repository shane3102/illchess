package pl.illchess.stockfish.application.position.command.out

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

interface LoadBoard {
    fun loadBoard(boardId: BoardId): FenBoardPosition?
}