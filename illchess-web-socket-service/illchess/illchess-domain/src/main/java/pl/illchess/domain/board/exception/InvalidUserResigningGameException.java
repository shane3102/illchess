package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.commons.exception.BadRequestException;

public class InvalidUserResigningGameException extends BadRequestException {
    public InvalidUserResigningGameException(Username username) {
        super("User: %s is not allowed to resign game".formatted(username));
    }
}
