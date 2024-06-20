package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.commons.exception.BadRequestException;

public class NoMovesPerformedException extends BadRequestException {
    public NoMovesPerformedException(BoardId boardId) {
        super("There are no moves performed on board with id = %s".formatted(boardId));
    }
}
