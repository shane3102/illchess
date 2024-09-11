package pl.illchess.player_info.domain.game.exception

import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.illchess.player_info.domain.game.model.GameResult

class GameResultNotRecognisedException(passedGameResult: String) : DomainException(
    "There is no game result with given name: $passedGameResult. Recognised game results: ${GameResult.entries}"
)