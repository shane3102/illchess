package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class InvalidUserResigningGameException extends BadRequestException {
    public InvalidUserResigningGameException(Username username) {
        super("User: %s is not allowed to resign game".formatted(username));
    }
}
