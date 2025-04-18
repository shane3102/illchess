package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.model.Piece;

import java.util.Set;

public class PieceCantMoveToGivenSquareException extends IllegalMoveException {

    public PieceCantMoveToGivenSquareException(
            Piece piece,
            Square targetSquare,
            Set<Square> possibleSquares,
            GameId gameId
    ) {
        super(
                "Piece %s on square %s can't move to square %s. Reachable squares are: %s".formatted(
                        piece.typeName().text(),
                        piece.square(),
                        targetSquare,
                        possibleSquares
                ),
            gameId,
                piece.square()
        );
    }

}
