package pl.illchess.stockfish.domain.board.exception

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.commons.NotFoundException

class BoardNotFoundException(gameId: GameId) :
    NotFoundException(String.format("Board with id  %s was not found", gameId))