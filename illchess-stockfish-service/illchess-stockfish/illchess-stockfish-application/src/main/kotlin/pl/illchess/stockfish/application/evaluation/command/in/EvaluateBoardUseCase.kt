package pl.illchess.stockfish.application.evaluation.command.`in`

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.evaluation.command.EvaluateBoard
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import java.util.*

interface EvaluateBoardUseCase {
    fun evaluateBoard(cmd: EvaluateBoardCmd): Evaluation

    data class EvaluateBoardCmd(
        val gameId: UUID,
    ) {
        fun toCommand(): EvaluateBoard = EvaluateBoard(GameId(gameId))
    }
}