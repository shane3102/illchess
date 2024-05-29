package pl.illchess.stockfish.adapter.evaluation.command.out.bash

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.bash.util.StockfishConnector
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation


@ApplicationScoped
class BashLoadBoardEvaluationAdapter : LoadBoardEvaluation {

    override fun loadBoardEvaluation(fenPosition: FenBoardPosition): Evaluation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine()
        return stockfishConnector.getEvaluation(fenPosition)
    }

}
