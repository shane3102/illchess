package pl.illchess.stockfish.application.evaluation.command.`in`

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.evaluation.command.EstablishBestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import java.util.UUID

interface EstablishBestMoveAndContinuationUseCase {

    fun establishBestMoveAndContinuation(cmd : EstablishBestMoveAndContinuationCmd): BestMoveAndContinuation

    data class EstablishBestMoveAndContinuationCmd(val boardId: UUID) {
        fun toCommand(): EstablishBestMoveAndContinuation = EstablishBestMoveAndContinuation(BoardId(boardId))
    }
}