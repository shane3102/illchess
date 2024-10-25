package pl.illchess.game.domain.board.event;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

public record BoardWithPreMovesOfUsernameUpdated(BoardId boardId, Username username) implements BoardWithPreMovesUpdated {
}
