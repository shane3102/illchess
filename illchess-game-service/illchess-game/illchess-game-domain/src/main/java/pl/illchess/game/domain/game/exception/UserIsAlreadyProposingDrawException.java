package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class UserIsAlreadyProposingDrawException extends BadRequestException {
    public UserIsAlreadyProposingDrawException(Username username) {
        super("User with username %s is already proposing draw".formatted(username));
    }
}
