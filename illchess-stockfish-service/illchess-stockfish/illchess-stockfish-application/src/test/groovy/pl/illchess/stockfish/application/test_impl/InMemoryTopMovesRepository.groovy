package pl.illchess.stockfish.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.SaveTopMoves
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

class InMemoryTopMovesRepository implements LoadTopMoves, SaveTopMoves {

    def repository = new HashMap<EvaluationBoardInformation, TopMoves>()

    @Override
    TopMoves loadTopMoves(@NotNull EvaluationBoardInformation evaluationBoardInformation) {
        return repository.get(evaluationBoardInformation)
    }

    @Override
    void saveTopMoves(@NotNull EvaluationBoardInformation evaluationBoardInformation, @NotNull TopMoves topMoves) {
        repository.put(evaluationBoardInformation, topMoves)
    }
}
