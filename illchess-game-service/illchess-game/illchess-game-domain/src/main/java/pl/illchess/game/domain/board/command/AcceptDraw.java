package pl.illchess.game.domain.board.command;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

public record AcceptDraw(BoardId boardId, Username username) {
}
