package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation

interface SaveBestMoveAndContinuation {
    fun saveBestMoveAndContinuation(
        evaluationBoardInformation: EvaluationBoardInformation,
        bestMoveAndContinuation: BestMoveAndContinuation
    )
}