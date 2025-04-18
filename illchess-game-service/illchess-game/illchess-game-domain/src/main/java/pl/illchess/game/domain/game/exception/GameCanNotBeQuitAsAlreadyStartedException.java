package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class GameCanNotBeQuitAsAlreadyStartedException extends BadRequestException {
    public GameCanNotBeQuitAsAlreadyStartedException(GameId gameId) {
        super("Game with id: %s can't be quit as game is already started".formatted(gameId.uuid()));
    }
}
