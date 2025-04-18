package pl.illchess.game.domain.game.event.pre_moves;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

public record GameWithPreMovesOfUsernameUpdated(GameId gameId, Username username) implements GameWithPreMovesUpdated {
}
