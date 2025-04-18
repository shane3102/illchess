package pl.illchess.stockfish.application.evaluation.command.`in`

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.evaluation.command.EstablishBestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import java.util.UUID

interface EstablishBestMoveAndContinuationUseCase {

    fun establishBestMoveAndContinuation(cmd : EstablishBestMoveAndContinuationCmd): BestMoveAndContinuation

    data class EstablishBestMoveAndContinuationCmd(
        val gameId: UUID,
        val depth: Int?
    ) {
        fun toCommand(): EstablishBestMoveAndContinuation = EstablishBestMoveAndContinuation(GameId(gameId), depth)
    }
}