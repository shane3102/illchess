package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.commons.exception.DomainException;

public abstract class IllegalMoveException extends DomainException {

    protected GameId gameId;
    protected Square highlightSquare;

    public IllegalMoveException(String message, GameId gameId, Square highlightSquare) {
        super(message);
        this.gameId = gameId;
        this.highlightSquare = highlightSquare;
    }

    public GameId getGameId() {
        return gameId;
    }

    public Square getHighlightSquare() {
        return highlightSquare;
    }

}
