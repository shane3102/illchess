package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.commons.exception.BadRequestException;
import pl.illchess.game.domain.piece.model.Piece;

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