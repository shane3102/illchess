package pl.illchess.stockfish.domain.evaluation.command

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

data class EstablishListOfTopMoves(
    val boardId: BoardId,
    val fenBoardPosition: FenBoardPosition,
    val moveCount: Int,
    val depth: Int?
)
