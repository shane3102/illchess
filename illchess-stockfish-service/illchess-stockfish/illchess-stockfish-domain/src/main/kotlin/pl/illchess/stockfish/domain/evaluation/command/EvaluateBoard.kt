package pl.illchess.stockfish.domain.evaluation.command

import pl.illchess.stockfish.domain.board.domain.BoardId

data class EvaluateBoard(val boardId: BoardId)
