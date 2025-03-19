package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation

interface LoadBoardEvaluation {
    fun loadBoardEvaluation(evaluationBoardInformation: EvaluationBoardInformation): Evaluation?
}