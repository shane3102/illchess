package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.commons.exception.DomainException;

import java.util.UUID;

public class GameNotFoundException extends DomainException {
    public GameNotFoundException(GameId gameId) {
        super("Game with given id = %s not found".formatted(gameId.uuid()));
    }

    public GameNotFoundException(UUID gameId) {
        super("Game with given id = %s not found".formatted(gameId));
    }
}
