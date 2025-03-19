package pl.illchess.stockfish.application.evaluation.command.`in`

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.evaluation.command.EstablishListOfTopMoves
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves
import java.util.UUID
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

interface EstablishListOfTopMovesUseCase {

    fun establishListOfTopMoves(cmd: EstablishListOfTopMovesCmd): TopMoves

    data class EstablishListOfTopMovesCmd(val boardId: UUID, val moveCount: Int, val depth: Int?) {
        fun toCommand(fenBoardPosition: FenBoardPosition) = EstablishListOfTopMoves(BoardId(boardId), fenBoardPosition, moveCount, depth)
    }
}