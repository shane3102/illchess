package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb_disabled

import io.quarkus.arc.properties.IfBuildProperty
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.SaveBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.SaveTopMoves
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

@ApplicationScoped
@IfBuildProperty(name = "calculations.caching.enabled", stringValue = "false")
class DisabledCachingMongodbAdapter :
    LoadBestMoveAndContinuation,
    SaveBestMoveAndContinuation,
    LoadBoardEvaluation,
    SaveBoardEvaluation,
    LoadTopMoves,
    SaveTopMoves
{
    override fun loadBestMoveAndContinuation(evaluationBoardInformation: EvaluationBoardInformation): BestMoveAndContinuation? {
        log.debug("Mongodb caching is disabled")
        return null
    }

    override fun saveBestMoveAndContinuation(
        evaluationBoardInformation: EvaluationBoardInformation,
        bestMoveAndContinuation: BestMoveAndContinuation
    ) {
        log.debug("Mongodb caching is disabled")
    }

    override fun loadBoardEvaluation(evaluationBoardInformation: EvaluationBoardInformation): Evaluation? {
        log.debug("Mongodb caching is disabled")
        return null
    }

    override fun saveBoardEvaluation(evaluationBoardInformation: EvaluationBoardInformation, evaluation: Evaluation) {
        log.debug("Mongodb caching is disabled")
    }

    override fun loadTopMoves(evaluationBoardInformation: EvaluationBoardInformation): TopMoves? {
        log.debug("Mongodb caching is disabled")
        return null
    }

    override fun saveTopMoves(evaluationBoardInformation: EvaluationBoardInformation, topMoves: TopMoves) {
        log.debug("Mongodb caching is disabled")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(DisabledCachingMongodbAdapter::class.java)
    }
}