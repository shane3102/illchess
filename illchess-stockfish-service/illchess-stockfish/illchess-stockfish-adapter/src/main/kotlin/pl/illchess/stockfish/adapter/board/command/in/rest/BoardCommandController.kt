package pl.illchess.stockfish.adapter.board.command.`in`.rest

import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.BestMoveAndContinuationResponse
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.EvaluationResponse
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.TopMovesResponse
import pl.illchess.stockfish.application.evaluation.command.`in`.EstablishBestMoveAndContinuationUseCase
import pl.illchess.stockfish.application.evaluation.command.`in`.EstablishListOfTopMovesUseCase
import pl.illchess.stockfish.application.evaluation.command.`in`.EvaluateBoardUseCase
import java.util.*

class BoardCommandController(
    private val evaluateBoardUseCase: EvaluateBoardUseCase,
    private val bestMoveAndContinuationUseCase: EstablishBestMoveAndContinuationUseCase,
    private val establishListOfTopMovesUseCase: EstablishListOfTopMovesUseCase
) : BoardCommandApi {

    override fun evaluateBoard(gameId: UUID): EvaluationResponse {
        val cmd = EvaluateBoardUseCase.EvaluateBoardCmd(gameId)
        val result = evaluateBoardUseCase.evaluateBoard(cmd)
        return EvaluationResponse(result.value)
    }

    override fun establishBestMoveAndContinuation(gameId: UUID, depth: Int?): BestMoveAndContinuationResponse {
        val cmd = EstablishBestMoveAndContinuationUseCase.EstablishBestMoveAndContinuationCmd(gameId, depth)
        val result = bestMoveAndContinuationUseCase.establishBestMoveAndContinuation(cmd)
        return BestMoveAndContinuationResponse(result.bestMove, result.continuation)
    }

    override fun establishTopMoves(moveCount: Int, gameId: UUID, depth: Int?): TopMovesResponse {
        val cmd = EstablishListOfTopMovesUseCase.EstablishListOfTopMovesCmd(gameId, moveCount, depth)
        val result = establishListOfTopMovesUseCase.establishListOfTopMoves(cmd)
        return TopMovesResponse(result.topMovesList)
    }

}