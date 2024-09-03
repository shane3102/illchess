package pl.illchess.player_info.domain.game.exception

import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.illchess.player_info.domain.game.model.GameId

class GameNotFoundException(gameId: GameId) : DomainException("Game with given id ${gameId.uuid} not found")