package pl.illchess.game.domain.game.command;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

public record RejectDraw(GameId gameId, Username username) {
}
