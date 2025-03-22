package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

interface CalculateTopMoves {
    fun calculateTopMoves(
        fenPosition: FenBoardPosition,
        topMoveCount: Int,
        depth: Int? = null
    ): TopMoves?
}