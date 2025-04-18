package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class InvalidUserIsProposingTakingBackMoveException extends BadRequestException {
    public InvalidUserIsProposingTakingBackMoveException(Username username) {
        super("User: %s is not allowed to propose draw in game".formatted(username));
    }
}
