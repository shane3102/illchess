package pl.illchess.stockfish.adapter.evaluation.command.out.bash

import io.quarkus.arc.properties.IfBuildProperty
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.illchess.stockfish.adapter.evaluation.command.out.bash.util.StockfishConnector
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation


@ApplicationScoped
@IfBuildProperty(name = "working.mode", stringValue = "ENGINE")
class BashStockfishAdapter : LoadBoardEvaluation, LoadBestMoveAndContinuation {

    @field:ConfigProperty(
        name = "stockfish.path",
        defaultValue = "stockfish"
    )
    lateinit var stockfishPath: String

    override fun loadBoardEvaluation(fenPosition: FenBoardPosition): Evaluation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine(stockfishPath)
        val result = stockfishConnector.getEvaluation(fenPosition)
        stockfishConnector.stopEngine()
        return result
    }

    override fun loadBestMoveAndContinuation(fenPosition: FenBoardPosition): BestMoveAndContinuation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine(stockfishPath)
        val result = stockfishConnector.getBestMoveAndContinuation(fenPosition)
        stockfishConnector.stopEngine()
        return result
    }

}
