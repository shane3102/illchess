package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.commons.exception.BadRequestException;

public class GameCanNotBeAcceptedOrRejectedAsDrawnException extends BadRequestException {
    public GameCanNotBeAcceptedOrRejectedAsDrawnException(BoardId boardId) {
        super("Game with id = %s can't be accepted or rejected as drawn".formatted(boardId));
    }
}
