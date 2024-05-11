package pl.illchess.stockfish.application.evaluation.command.out

import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

interface LoadBoardEvaluation {
    fun loadBoardEvaluation(fenPosition: FenBoardPosition): Evaluation?
}