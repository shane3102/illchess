package pl.illchess.stockfish.adapter.board.command.`in`.rest

import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.EvaluationResponse
import pl.illchess.stockfish.application.evaluation.command.`in`.EvaluateBoardUseCase
import java.util.*

class BoardCommandController(private val evaluateBoardUseCase: EvaluateBoardUseCase) : BoardCommandApi {

    override fun evaluateBoard(boardId: UUID): EvaluationResponse {
        val cmd = EvaluateBoardUseCase.EvaluateBoardCmd(boardId)
        val result = evaluateBoardUseCase.evaluateBoard(cmd)
        return EvaluationResponse(result.value)
    }

}