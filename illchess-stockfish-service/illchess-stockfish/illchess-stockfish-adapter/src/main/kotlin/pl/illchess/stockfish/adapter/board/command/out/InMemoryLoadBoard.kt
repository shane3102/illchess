package pl.illchess.stockfish.adapter.board.command.out

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.application.position.command.out.LoadBoard
import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

@ApplicationScoped
class InMemoryLoadBoard : LoadBoard {
    override fun loadBoard(boardId: BoardId): FenBoardPosition? {
        return FenBoardPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    }
}