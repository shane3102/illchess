package pl.illchess.stockfish.adapter.evaluation.command.out.inmemory

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.SaveTopMoves
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

@ApplicationScoped
class InMemoryTopMovesRepository(
    private val repository: HashMap<EvaluationBoardInformation, TopMoves> = hashMapOf()
) : LoadTopMoves, SaveTopMoves {
    override fun loadTopMoves(
        evaluationBoardInformation: EvaluationBoardInformation
    ) = repository[evaluationBoardInformation]

    override fun saveTopMoves(
        evaluationBoardInformation: EvaluationBoardInformation,
        topMoves: TopMoves
    ) {
        repository[evaluationBoardInformation] = topMoves
    }
}

