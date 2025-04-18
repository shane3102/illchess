package pl.illchess.game.domain.game.event.pre_moves;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.commons.event.DomainEvent;

public interface GameWithPreMovesUpdated extends DomainEvent {
    GameId gameId();

    Username username();
}
