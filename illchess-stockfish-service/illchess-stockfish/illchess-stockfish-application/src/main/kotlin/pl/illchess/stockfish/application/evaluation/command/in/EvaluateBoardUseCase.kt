package pl.illchess.stockfish.application.evaluation.command.`in`

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.evaluation.command.EvaluateBoard
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import java.util.*

interface EvaluateBoardUseCase {
    fun evaluateBoard(cmd: EvaluateBoardCmd): Evaluation

    data class EvaluateBoardCmd(val boardId: UUID) {
        fun toCommand(): EvaluateBoard = EvaluateBoard(BoardId(boardId))
    }
}