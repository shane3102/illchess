package pl.illchess.stockfish.adapter.evaluation.command.out.inmemory

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBoardEvaluation
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation

@ApplicationScoped
class InMemoryBoardEvaluationRepository(
    private val repository: HashMap<EvaluationBoardInformation, Evaluation> = hashMapOf()
) : LoadBoardEvaluation, SaveBoardEvaluation {
    override fun loadBoardEvaluation(
        evaluationBoardInformation: EvaluationBoardInformation
    ) = repository[evaluationBoardInformation]

    override fun saveBoardEvaluation(
        evaluationBoardInformation: EvaluationBoardInformation,
        evaluation: Evaluation
    ) {
        repository[evaluationBoardInformation] = evaluation
    }
}