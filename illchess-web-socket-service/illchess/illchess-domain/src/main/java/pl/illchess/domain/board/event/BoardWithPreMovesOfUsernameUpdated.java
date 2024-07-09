package pl.illchess.domain.board.event;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

public record BoardWithPreMovesOfUsernameUpdated(BoardId boardId, Username username) implements BoardWithPreMovesUpdated {
}
