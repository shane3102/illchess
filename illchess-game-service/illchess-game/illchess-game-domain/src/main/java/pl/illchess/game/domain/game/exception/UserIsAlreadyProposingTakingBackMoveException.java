package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class UserIsAlreadyProposingTakingBackMoveException extends BadRequestException {
    public UserIsAlreadyProposingTakingBackMoveException(Username username) {
        super("User with username %s is already proposing taking back move".formatted(username));
    }
}
