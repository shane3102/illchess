package pl.illchess.stockfish.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.stockfish.application.board.command.out.LoadGame
import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

class InMemoryGameRepository implements LoadGame {

    def defaultFenBoardPosition

    InMemoryGameRepository(FenBoardPosition defaultFenBoardPosition) {
        this.defaultFenBoardPosition = defaultFenBoardPosition
    }

    @Override
    FenBoardPosition loadGame(@NotNull GameId gameId) {
        return defaultFenBoardPosition
    }
}
