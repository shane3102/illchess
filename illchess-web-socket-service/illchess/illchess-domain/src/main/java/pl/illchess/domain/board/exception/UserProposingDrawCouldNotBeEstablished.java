package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.commons.exception.BadRequestException;

public class UserProposingDrawCouldNotBeEstablished extends BadRequestException {
    public UserProposingDrawCouldNotBeEstablished(BoardId boardId) {
        super("User proposing taking back last move on board %s couldn't be established".formatted(boardId));
    }
}
