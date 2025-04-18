package pl.illchess.stockfish.domain.evaluation.command

import pl.illchess.stockfish.domain.board.domain.GameId

data class EvaluateBoard(val gameId: GameId)
