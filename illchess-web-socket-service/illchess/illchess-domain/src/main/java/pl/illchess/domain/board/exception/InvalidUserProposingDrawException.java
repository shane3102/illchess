package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.commons.exception.BadRequestException;

public class InvalidUserProposingDrawException extends BadRequestException {
    public InvalidUserProposingDrawException(Username username) {
        super("User: %s is not allowed to propose draw in game".formatted(username));
    }
}
