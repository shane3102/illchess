package pl.illchess.game.domain.game.event.delete;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.commons.event.DomainEvent;

public interface GameDeleteInfo extends DomainEvent {
    GameId gameId();
}
