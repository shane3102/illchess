package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.FenBoardString;
import pl.illchess.domain.board.model.state.player.Username;

public record JoinOrInitializeNewGame(
    Username username,
    FenBoardString fen
) {
}
