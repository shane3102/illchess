package pl.illchess.player_info.domain.game.event

import pl.illchess.player_info.domain.commons.event.DomainEvent
import pl.illchess.player_info.domain.game.model.GameId

data class GameSavedEvent(val gameId: GameId) : DomainEvent
