package pl.illchess.stockfish.domain.board.exception

import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.commons.NotFoundException

class BoardNotFoundException(boardId: BoardId) :
    NotFoundException(String.format("Board with id  %s was not found", boardId))