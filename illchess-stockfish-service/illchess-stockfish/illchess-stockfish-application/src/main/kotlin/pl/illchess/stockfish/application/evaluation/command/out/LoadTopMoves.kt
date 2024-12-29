package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

interface LoadTopMoves {
    fun loadTopMoves(
        fenPosition: FenBoardPosition,
        topMoveCount: Int,
        depth: Int? = null
    ): TopMoves?
}