package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation

interface SaveBoardEvaluation {
    fun saveBoardEvaluation(
        evaluationBoardInformation: EvaluationBoardInformation,
        evaluation: Evaluation
    )
}