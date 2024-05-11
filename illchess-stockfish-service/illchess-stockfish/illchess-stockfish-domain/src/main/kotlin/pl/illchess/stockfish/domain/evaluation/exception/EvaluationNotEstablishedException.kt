package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.commons.BadRequestException

class EvaluationNotEstablishedException(boardId: BoardId) :
    BadRequestException(String.format("Evaluation of board with id = %s could not be obtained", boardId))