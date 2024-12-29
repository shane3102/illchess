package pl.illchess.stockfish.adapter.evaluation.command.out.bash

import io.quarkus.arc.properties.IfBuildProperty
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.illchess.stockfish.adapter.evaluation.command.out.bash.util.StockfishConnector
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves


@ApplicationScoped
@IfBuildProperty(name = "working.mode", stringValue = "ENGINE")
class BashStockfishAdapter : LoadBoardEvaluation, LoadBestMoveAndContinuation, LoadTopMoves {

    @field:ConfigProperty(
        name = "stockfish.default-depth",
        defaultValue = "15"
    )
    lateinit var defaultDepth: String

    @field:ConfigProperty(
        name = "stockfish.path",
        defaultValue = "stockfish"
    )
    lateinit var stockfishPath: String

    override fun loadBoardEvaluation(
        fenPosition: FenBoardPosition,
    ): Evaluation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine(stockfishPath)
        val result = stockfishConnector.getEvaluation(fenPosition)
        stockfishConnector.stopEngine()
        return result
    }

    override fun loadBestMoveAndContinuation(
        fenPosition: FenBoardPosition,
        depth: Int?
    ): BestMoveAndContinuation? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine(stockfishPath)
        val result = stockfishConnector.getBestMoveAndContinuation(
            fenPosition,
            depth ?: defaultDepth.toInt()
        )
        stockfishConnector.stopEngine()
        return result
    }

    override fun loadTopMoves(
        fenPosition: FenBoardPosition,
        topMoveCount: Int,
        depth: Int?
    ): TopMoves? {
        val stockfishConnector = StockfishConnector()
        stockfishConnector.startEngine(stockfishPath)
        val result = stockfishConnector.getTopMovesByNumber(
            fenPosition,
            topMoveCount,
            depth ?: defaultDepth.toInt()
        )
        stockfishConnector.stopEngine()
        return result
    }


}
