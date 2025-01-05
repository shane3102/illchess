package pl.illchess.player_info.domain.game.exception

import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.illchess.player_info.domain.game.model.GameId

class GameAlreadyExistsException(id: GameId): DomainException(
    "Game with id = ${id.uuid} already exists"
)