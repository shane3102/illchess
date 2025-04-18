package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.commons.BadRequestException

class BestMoveAndContinuationCouldNotBeEstablished(gameId: GameId):
    BadRequestException("Best move and continuation couldn't be established on board with id = %s".format(gameId))