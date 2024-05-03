package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.commons.exception.BadRequestException;

public class UserIsAlreadyProposingDrawException extends BadRequestException {
    public UserIsAlreadyProposingDrawException(Username username) {
        super("User with username %s is already proposing draw".formatted(username));
    }
}
