package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation

interface LoadBestMoveAndContinuation {
    fun loadBestMoveAndContinuation(evaluationBoardInformation: EvaluationBoardInformation): BestMoveAndContinuation?
}