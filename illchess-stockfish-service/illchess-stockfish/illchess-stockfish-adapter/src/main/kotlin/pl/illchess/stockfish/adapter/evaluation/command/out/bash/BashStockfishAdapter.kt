package pl.illchess.stockfish.adapter.evaluation.command.out.bash

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.bash.util.StockfishConnector
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation


@ApplicationScoped
class BashStockfishAdapter : LoadBoardEvaluation, LoadBestMoveAndContinuation {

    override fun loadBoardEvaluation(fenPosition: FenBoardPosition): Evaluation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine()
        return stockfishConnector.getEvaluation(fenPosition)
    }

    override fun loadBestMoveAndContinuation(fenPosition: FenBoardPosition): BestMoveAndContinuation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine()
        return stockfishConnector.getBestMoveAndContinuation(fenPosition)
    }

}
