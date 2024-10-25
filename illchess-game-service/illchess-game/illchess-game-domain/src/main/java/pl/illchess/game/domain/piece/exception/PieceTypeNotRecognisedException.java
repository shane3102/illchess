package pl.illchess.game.domain.piece.exception;

import pl.illchess.game.domain.commons.exception.DomainException;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.info.PieceType;

public class PieceTypeNotRecognisedException extends DomainException {
    public PieceTypeNotRecognisedException(PieceType pieceType) {
        super("Piece type = %s was not recognised".formatted(pieceType.text()));
    }

    public PieceTypeNotRecognisedException(Class<? extends Piece> pieceClass) {
        super("Piece of class = %s was not recognised".formatted(pieceClass));
    }
}
