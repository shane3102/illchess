package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation

interface LoadBestMoveAndContinuation {
    fun loadBestMoveAndContinuation(
        fenPosition: FenBoardPosition,
        depth: Int? = null
    ): BestMoveAndContinuation?
}