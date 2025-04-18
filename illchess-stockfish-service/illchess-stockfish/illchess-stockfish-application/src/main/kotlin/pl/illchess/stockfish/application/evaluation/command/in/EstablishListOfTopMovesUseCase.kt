package pl.illchess.stockfish.application.evaluation.command.`in`

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.evaluation.command.EstablishListOfTopMoves
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves
import java.util.UUID
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

interface EstablishListOfTopMovesUseCase {

    fun establishListOfTopMoves(cmd: EstablishListOfTopMovesCmd): TopMoves

    data class EstablishListOfTopMovesCmd(val gameId: UUID, val moveCount: Int, val depth: Int?) {
        fun toCommand(fenBoardPosition: FenBoardPosition) = EstablishListOfTopMoves(GameId(gameId), fenBoardPosition, moveCount, depth)
    }
}