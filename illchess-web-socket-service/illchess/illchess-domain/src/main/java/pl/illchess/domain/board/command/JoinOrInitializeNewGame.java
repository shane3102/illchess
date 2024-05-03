package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.state.FenString;
import pl.illchess.domain.board.model.state.player.Username;

public record JoinOrInitializeNewGame(
    Username username,
    FenString fen
) {
}
