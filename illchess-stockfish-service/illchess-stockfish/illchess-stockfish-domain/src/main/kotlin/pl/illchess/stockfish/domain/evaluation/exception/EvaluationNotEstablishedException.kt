package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.commons.BadRequestException

class EvaluationNotEstablishedException(gameId: GameId) :
    BadRequestException(String.format("Evaluation of board with id = %s could not be obtained", gameId))