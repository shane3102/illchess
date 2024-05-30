package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.commons.BadRequestException

class BestMoveAndContinuationCouldNotBeEstablished(boardId: BoardId):
    BadRequestException("Best move and continuation couldn't be established on board with id = %s".format(boardId))