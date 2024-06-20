package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

public record AcceptTakingBackMove(BoardId boardId, Username username) {
}
