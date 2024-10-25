package pl.illchess.game.domain.board.command;

import pl.illchess.game.domain.board.model.FenBoardString;
import pl.illchess.game.domain.board.model.state.player.Username;

public record JoinOrInitializeNewGame(
    Username username,
    FenBoardString fen
) {
}
