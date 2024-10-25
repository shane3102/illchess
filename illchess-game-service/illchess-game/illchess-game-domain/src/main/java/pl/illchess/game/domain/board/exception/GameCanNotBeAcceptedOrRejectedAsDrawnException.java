package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class GameCanNotBeAcceptedOrRejectedAsDrawnException extends BadRequestException {
    public GameCanNotBeAcceptedOrRejectedAsDrawnException(BoardId boardId) {
        super("Game with id = %s can't be accepted or rejected as drawn".formatted(boardId));
    }
}
