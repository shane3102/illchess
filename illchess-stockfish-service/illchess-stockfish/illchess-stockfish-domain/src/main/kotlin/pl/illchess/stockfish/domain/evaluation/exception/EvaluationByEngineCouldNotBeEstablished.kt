package pl.illchess.stockfish.domain.evaluation.exception

import pl.illchess.stockfish.domain.commons.BadRequestException

class EvaluationByEngineCouldNotBeEstablished :
    BadRequestException("Evaluation of board could not be established by engine")