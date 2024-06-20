package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.commons.exception.BadRequestException;

public class InvalidUserIsProposingTakingBackMoveException extends BadRequestException {
    public InvalidUserIsProposingTakingBackMoveException(Username username) {
        super("User: %s is not allowed to propose draw in game".formatted(username));
    }
}
