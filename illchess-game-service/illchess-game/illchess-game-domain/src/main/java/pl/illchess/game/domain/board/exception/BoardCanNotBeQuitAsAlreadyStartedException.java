package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class BoardCanNotBeQuitAsAlreadyStartedException extends BadRequestException {
    public BoardCanNotBeQuitAsAlreadyStartedException(BoardId boardId) {
        super("Board with id: %s can't be quit as game is already started".formatted(boardId.uuid()));
    }
}
