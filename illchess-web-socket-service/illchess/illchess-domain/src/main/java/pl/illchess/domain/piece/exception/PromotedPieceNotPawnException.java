package pl.illchess.domain.piece.exception;

import pl.illchess.domain.commons.exception.DomainException;

public class PromotedPieceNotPawnException extends DomainException {
    public PromotedPieceNotPawnException() {
        super("Piece marked as promoted was not pawn type");
    }
}
