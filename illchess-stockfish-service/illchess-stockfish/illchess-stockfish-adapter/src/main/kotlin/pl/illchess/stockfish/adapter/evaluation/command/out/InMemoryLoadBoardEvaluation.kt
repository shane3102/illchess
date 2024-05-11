package pl.illchess.stockfish.adapter.evaluation.command.out

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation

@ApplicationScoped
class InMemoryLoadBoardEvaluation : LoadBoardEvaluation {
    override fun loadBoardEvaluation(fenPosition: FenBoardPosition): Evaluation? {
        return Evaluation(Math.random())
    }
}