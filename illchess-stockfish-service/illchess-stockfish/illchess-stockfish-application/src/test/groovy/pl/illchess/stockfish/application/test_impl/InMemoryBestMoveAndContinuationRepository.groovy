package pl.illchess.stockfish.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBestMoveAndContinuation
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation

class InMemoryBestMoveAndContinuationRepository implements LoadBestMoveAndContinuation, SaveBestMoveAndContinuation{

    def repository = new HashMap<EvaluationBoardInformation, BestMoveAndContinuation>()

    @Override
    BestMoveAndContinuation loadBestMoveAndContinuation(@NotNull EvaluationBoardInformation evaluationBoardInformation) {
        return repository.get(evaluationBoardInformation)
    }

    @Override
    void saveBestMoveAndContinuation(@NotNull EvaluationBoardInformation evaluationBoardInformation, @NotNull BestMoveAndContinuation bestMoveAndContinuation) {
        repository.put(evaluationBoardInformation, bestMoveAndContinuation)
    }
}
