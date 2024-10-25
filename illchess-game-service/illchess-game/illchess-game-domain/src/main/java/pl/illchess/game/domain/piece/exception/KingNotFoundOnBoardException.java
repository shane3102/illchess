package pl.illchess.game.domain.piece.exception;

import pl.illchess.game.domain.commons.exception.DomainException;

public class KingNotFoundOnBoardException extends DomainException {
    public KingNotFoundOnBoardException() {
        super("King not found on board");
    }
}
