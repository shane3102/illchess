package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.commons.BadRequestException

class TopMovesNotAvailableWhenUsingApi: BadRequestException("Top moves endpoint is not available when using engine by api")