package pl.illchess.stockfish.adapter.evaluation.command.out.bash

import io.quarkus.arc.properties.IfBuildProperty
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.bash.util.StockfishConnector
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation


@ApplicationScoped
@IfBuildProperty(name = "working.mode", stringValue = "ENGINE")
class BashStockfishAdapter : LoadBoardEvaluation, LoadBestMoveAndContinuation {

    override fun loadBoardEvaluation(fenPosition: FenBoardPosition): Evaluation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine()
        val result = stockfishConnector.getEvaluation(fenPosition)
        stockfishConnector.stopEngine()
        return result
    }

    override fun loadBestMoveAndContinuation(fenPosition: FenBoardPosition): BestMoveAndContinuation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine()
        val result = stockfishConnector.getBestMoveAndContinuation(fenPosition)
        stockfishConnector.stopEngine()
        return result
    }

}
