package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.commons.BadRequestException

class TopMovesCouldNotBeEstablished(gameId: GameId): BadRequestException("Top moves on board with id = %s could not be established".format(gameId.uuid))