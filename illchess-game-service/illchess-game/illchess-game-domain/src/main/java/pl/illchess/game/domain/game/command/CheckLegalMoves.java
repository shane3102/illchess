package pl.illchess.game.domain.game.command;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.player.Username;

public record CheckLegalMoves(
    GameId gameId,
    Square square,
    Username username
) {
}
