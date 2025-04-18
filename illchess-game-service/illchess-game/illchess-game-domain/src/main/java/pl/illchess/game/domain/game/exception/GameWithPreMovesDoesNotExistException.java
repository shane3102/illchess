package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class GameWithPreMovesDoesNotExistException extends BadRequestException {
    public GameWithPreMovesDoesNotExistException(GameId gameId, Username username) {
        super("User with username = %s in game with id = %s does not have any pre-moves scheduled".formatted(gameId, username));
    }
}
