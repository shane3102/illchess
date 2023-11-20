package pl.illchess.domain.board.exception;

import pl.illchess.domain.commons.exception.DomainException;
import pl.illchess.domain.piece.info.PieceColor;

public class PieceColorIncorrectException extends DomainException {
    public PieceColorIncorrectException(PieceColor movedPieceColor, PieceColor currentPlayerColor) {
        super(
                "Moved piece color is incorrect, %s color piece was moved but %s should be moved"
                        .formatted(movedPieceColor, currentPlayerColor)
        );
    }
}
