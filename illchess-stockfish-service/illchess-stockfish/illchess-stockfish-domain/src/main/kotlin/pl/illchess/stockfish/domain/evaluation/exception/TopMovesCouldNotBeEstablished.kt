package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.commons.BadRequestException

class TopMovesCouldNotBeEstablished(boardId: BoardId): BadRequestException("Top moves on board with id = %s could not be established".format(boardId.uuid))