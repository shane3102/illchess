package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.commons.exception.BadRequestException;
import pl.illchess.domain.piece.model.Piece;

public class PieceIsAlreadyOnSquareUserTriesToPlaceItOnException extends BadRequestException {
    public PieceIsAlreadyOnSquareUserTriesToPlaceItOnException(
        Piece piece,
        Square targetSquare
    ) {
        super(
            "Piece %s is already on square %s.".formatted(
                piece.typeName().text(),
                targetSquare
            )
        );
    }
}
