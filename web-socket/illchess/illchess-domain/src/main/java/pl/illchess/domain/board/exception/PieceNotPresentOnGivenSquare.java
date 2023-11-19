package pl.illchess.domain.board.exception;

import pl.illchess.domain.commons.exception.DomainException;

public class PieceNotPresentOnGivenSquare extends DomainException {
    public PieceNotPresentOnGivenSquare() {
        super("Piece not present on given square");
    }
}
