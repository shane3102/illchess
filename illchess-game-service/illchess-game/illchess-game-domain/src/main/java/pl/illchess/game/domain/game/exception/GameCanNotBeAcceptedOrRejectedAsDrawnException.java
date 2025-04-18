package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class GameCanNotBeAcceptedOrRejectedAsDrawnException extends BadRequestException {
    public GameCanNotBeAcceptedOrRejectedAsDrawnException(GameId gameId) {
        super("Game with id = %s can't be accepted or rejected as drawn".formatted(gameId));
    }
}
