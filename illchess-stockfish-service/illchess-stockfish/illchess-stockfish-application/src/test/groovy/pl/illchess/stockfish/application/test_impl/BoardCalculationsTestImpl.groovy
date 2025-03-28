package pl.illchess.stockfish.application.test_impl

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import pl.illchess.stockfish.application.evaluation.command.out.CalculateBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.CalculateBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.CalculateTopMoves
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

class BoardCalculationsTestImpl implements CalculateBoardEvaluation, CalculateBestMoveAndContinuation, CalculateTopMoves {
    @Override
    Evaluation calculateBoardEvaluation(@NotNull FenBoardPosition fenPosition) {
        return new Evaluation(0.69)
    }

    @Override
    BestMoveAndContinuation calculateBestMoveAndContinuation(@NotNull FenBoardPosition fenPosition, @Nullable Integer depth) {
        return new BestMoveAndContinuation("a1a3", ["a1a3"])
    }

    @Override
    TopMoves calculateTopMoves(@NotNull FenBoardPosition fenPosition, int topMoveCount, @Nullable Integer depth) {
        def topMoves = ["a1a3", "b1b3", "c1c3", "d1d3", "e1e3", "f1f3", "g1g3", "h1h3"]
        return new TopMoves(topMoves.stream().limit(topMoveCount).toList())
    }
}
