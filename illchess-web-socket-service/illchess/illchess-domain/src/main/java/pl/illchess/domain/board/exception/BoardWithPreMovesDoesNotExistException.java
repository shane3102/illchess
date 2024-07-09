package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.commons.exception.BadRequestException;

public class BoardWithPreMovesDoesNotExistException extends BadRequestException {
    public BoardWithPreMovesDoesNotExistException(BoardId boardId, Username username) {
        super("On board with id = %s user with username = %s does not have any pre-moves scheduled".formatted(boardId, username));
    }
}
