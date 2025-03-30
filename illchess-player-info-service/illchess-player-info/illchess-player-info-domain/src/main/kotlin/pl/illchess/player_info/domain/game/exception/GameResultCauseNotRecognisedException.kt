package pl.illchess.player_info.domain.game.exception

import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.illchess.player_info.domain.game.model.GameResultCause

class GameResultCauseNotRecognisedException(passedGameResultCause: String): DomainException(
    "Game result cause of type: $passedGameResultCause not recognised. Recognised game result causes: ${GameResultCause.entries}"
)