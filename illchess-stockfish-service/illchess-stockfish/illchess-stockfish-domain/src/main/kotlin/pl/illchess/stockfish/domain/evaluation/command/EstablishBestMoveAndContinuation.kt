package pl.illchess.stockfish.domain.evaluation.command

import pl.illchess.stockfish.domain.board.domain.BoardId

data class EstablishBestMoveAndContinuation(val boardId: BoardId, val depth: Int?)
