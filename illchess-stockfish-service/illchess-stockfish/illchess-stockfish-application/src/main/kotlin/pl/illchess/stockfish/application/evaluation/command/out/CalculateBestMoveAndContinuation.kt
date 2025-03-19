package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation

interface CalculateBestMoveAndContinuation {
    fun calculateBestMoveAndContinuation(
        fenPosition: FenBoardPosition,
        depth: Int? = null
    ): BestMoveAndContinuation?
}