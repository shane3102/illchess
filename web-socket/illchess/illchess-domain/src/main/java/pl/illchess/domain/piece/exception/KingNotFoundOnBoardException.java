package pl.illchess.domain.piece.exception;

import pl.illchess.domain.commons.exception.DomainException;

public class KingNotFoundOnBoardException extends DomainException {
    public KingNotFoundOnBoardException() {
        super("King not found on board");
    }
}
