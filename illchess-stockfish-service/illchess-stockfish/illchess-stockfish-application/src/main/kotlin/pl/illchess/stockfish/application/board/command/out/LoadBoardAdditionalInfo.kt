package pl.illchess.stockfish.application.board.command.out

import pl.illchess.stockfish.domain.board.domain.BoardAdditionalInfo
import pl.illchess.stockfish.domain.board.domain.BoardId

interface LoadBoardAdditionalInfo {
    fun loadBoardAdditionalInfo(boardId: BoardId): BoardAdditionalInfo?
}