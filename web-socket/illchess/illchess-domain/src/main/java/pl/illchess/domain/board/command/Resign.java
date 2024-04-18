package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

public record Resign(
    BoardId boardId,
    Username username
) {
}
