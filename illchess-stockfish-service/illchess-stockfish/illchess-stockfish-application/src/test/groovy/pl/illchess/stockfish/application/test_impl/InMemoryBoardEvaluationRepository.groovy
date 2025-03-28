package pl.illchess.stockfish.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBoardEvaluation
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation

class InMemoryBoardEvaluationRepository implements LoadBoardEvaluation, SaveBoardEvaluation {

    def repository = new HashMap<EvaluationBoardInformation, Evaluation>()

    @Override
    Evaluation loadBoardEvaluation(@NotNull EvaluationBoardInformation evaluationBoardInformation) {
        return repository.get(evaluationBoardInformation)
    }

    @Override
    void saveBoardEvaluation(@NotNull EvaluationBoardInformation evaluationBoardInformation, @NotNull Evaluation evaluation) {
        repository.put(evaluationBoardInformation, evaluation)
    }
}
