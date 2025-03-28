package pl.illchess.stockfish.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.stockfish.application.board.command.out.LoadBoard
import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

class InMemoryBoardRepository implements LoadBoard {

    def defaultFenBoardPosition

    InMemoryBoardRepository(FenBoardPosition defaultFenBoardPosition) {
        this.defaultFenBoardPosition = defaultFenBoardPosition
    }

    @Override
    FenBoardPosition loadBoard(@NotNull BoardId boardId) {
        return defaultFenBoardPosition
    }
}
