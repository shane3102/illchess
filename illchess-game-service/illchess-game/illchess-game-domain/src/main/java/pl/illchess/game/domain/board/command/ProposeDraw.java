package pl.illchess.game.domain.board.command;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

public record ProposeDraw(BoardId boardId, Username username) {
}
