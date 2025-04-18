package pl.illchess.game.domain.game.event;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.commons.event.DomainEvent;

public interface GameAdditionalInfoUpdated extends DomainEvent {
    GameId gameId();
}
