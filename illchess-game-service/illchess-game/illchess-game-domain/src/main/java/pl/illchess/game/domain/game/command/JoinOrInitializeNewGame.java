package pl.illchess.game.domain.game.command;

import pl.illchess.game.domain.game.model.FenBoardString;
import pl.illchess.game.domain.game.model.state.player.Username;

public record JoinOrInitializeNewGame(
    Username username,
    FenBoardString fen
) {
}
