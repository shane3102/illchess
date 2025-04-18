package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class UserProposingDrawCouldNotBeEstablished extends BadRequestException {
    public UserProposingDrawCouldNotBeEstablished(GameId gameId) {
        super("User proposing taking back last move on board %s couldn't be established".formatted(gameId));
    }
}
