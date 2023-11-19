package pl.illchess.domain.board.exception;

import pl.illchess.domain.commons.exception.DomainException;

public class TargetSquareOccupiedBySameColorPieceException extends DomainException {
    public TargetSquareOccupiedBySameColorPieceException() {
        super("Target square occupied by same color piece");
    }
}
