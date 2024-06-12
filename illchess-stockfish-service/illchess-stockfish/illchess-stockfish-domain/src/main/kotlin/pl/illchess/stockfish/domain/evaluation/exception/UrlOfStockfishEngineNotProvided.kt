package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.commons.NotFoundException

class UrlOfStockfishEngineNotProvided: NotFoundException("Url of stockfish engine wasn't provided") {
}