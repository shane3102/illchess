package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.commons.exception.BadRequestException;

public class UserIsAlreadyProposingTakingBackMoveException extends BadRequestException {
    public UserIsAlreadyProposingTakingBackMoveException(Username username) {
        super("User with username %s is already proposing taking back move".formatted(username));
    }
}
