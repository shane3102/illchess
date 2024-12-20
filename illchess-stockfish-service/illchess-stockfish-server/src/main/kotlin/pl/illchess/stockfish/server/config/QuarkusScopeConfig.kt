package pl.illchess.stockfish.server.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import pl.illchess.stockfish.application.evaluation.command.EvaluateBoardService
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.position.command.out.LoadBoard

class QuarkusScopeConfig {

    @Produces
    @ApplicationScoped
    fun evaluateBoardService(
        loadBoardEvaluation: LoadBoardEvaluation,
        loadBoard: LoadBoard,
        loadBestMoveAndContinuation: LoadBestMoveAndContinuation,
        loadTopMoves: LoadTopMoves
    ): EvaluateBoardService {
        return EvaluateBoardService(loadBoard, loadBoardEvaluation, loadBestMoveAndContinuation, loadTopMoves)
    }
}