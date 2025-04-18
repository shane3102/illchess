package pl.illchess.stockfish.domain.evaluation.command

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

data class EstablishListOfTopMoves(
    val gameId: GameId,
    val fenBoardPosition: FenBoardPosition,
    val moveCount: Int,
    val depth: Int?
)
