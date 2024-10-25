package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class UserProposingDrawCouldNotBeEstablished extends BadRequestException {
    public UserProposingDrawCouldNotBeEstablished(BoardId boardId) {
        super("User proposing taking back last move on board %s couldn't be established".formatted(boardId));
    }
}
