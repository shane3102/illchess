package pl.illchess.stockfish.domain.evaluation.command

import pl.illchess.stockfish.domain.board.domain.BoardId

data class EstablishListOfTopMoves(val boardId: BoardId, val moveCount: Int, val depth: Int?)
