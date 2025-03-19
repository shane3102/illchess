package pl.illchess.stockfish.adapter.evaluation.command.out.inmemory

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBestMoveAndContinuation
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation

@ApplicationScoped
class InMemoryBestMoveAndContinuationRepository(
    private val repository: HashMap<EvaluationBoardInformation, BestMoveAndContinuation> = hashMapOf()
) : LoadBestMoveAndContinuation, SaveBestMoveAndContinuation {
    override fun loadBestMoveAndContinuation(
        evaluationBoardInformation: EvaluationBoardInformation
    ) = repository[evaluationBoardInformation]

    override fun saveBestMoveAndContinuation(
        evaluationBoardInformation: EvaluationBoardInformation,
        bestMoveAndContinuation: BestMoveAndContinuation
    ) {
        repository[evaluationBoardInformation] = bestMoveAndContinuation
    }
}