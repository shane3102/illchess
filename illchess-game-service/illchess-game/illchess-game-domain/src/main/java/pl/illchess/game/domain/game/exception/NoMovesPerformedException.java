package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class NoMovesPerformedException extends BadRequestException {
    public NoMovesPerformedException(GameId gameId) {
        super("There are no moves performed on board with id = %s".formatted(gameId));
    }
}
