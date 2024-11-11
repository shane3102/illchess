package pl.illchess.stockfish.application.evaluation.command.`in`

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.evaluation.command.EstablishListOfTopMoves
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves
import java.util.UUID

interface EstablishListOfTopMovesUseCase {

    fun establishListOfTopMoves(cmd: EstablishListOfTopMovesCmd): TopMoves

    data class EstablishListOfTopMovesCmd(val boardId: UUID, val moveCount: Int) {
        fun toCommand() = EstablishListOfTopMoves(BoardId(boardId), moveCount)
    }
}