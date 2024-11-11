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

    override fun evaluateBoard(boardId: UUID): EvaluationResponse {
        val cmd = EvaluateBoardUseCase.EvaluateBoardCmd(boardId)
        val result = evaluateBoardUseCase.evaluateBoard(cmd)
        return EvaluationResponse(result.value)
    }

    override fun establishBestMoveAndContinuation(boardId: UUID): BestMoveAndContinuationResponse {
        val cmd = EstablishBestMoveAndContinuationUseCase.EstablishBestMoveAndContinuationCmd(boardId)
        val result = bestMoveAndContinuationUseCase.establishBestMoveAndContinuation(cmd)
        return BestMoveAndContinuationResponse(result.bestMove, result.continuation)
    }

    override fun establishTopMoves(moveCount: Int,boardId: UUID): TopMovesResponse {
        val cmd = EstablishListOfTopMovesUseCase.EstablishListOfTopMovesCmd(boardId, moveCount)
        val result = establishListOfTopMovesUseCase.establishListOfTopMoves(cmd)
        return TopMovesResponse(result.topMovesList)
    }

}