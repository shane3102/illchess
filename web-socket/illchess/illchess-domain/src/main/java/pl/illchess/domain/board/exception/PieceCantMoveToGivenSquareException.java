package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.PieceBehaviour;

import java.util.Set;

public class PieceCantMoveToGivenSquareException extends IllegalMoveException {

    public PieceCantMoveToGivenSquareException(
            PieceBehaviour piece,
            Square targetSquare,
            Set<Square> possibleSquares,
            BoardId boardId
    ) {
        super(
                "Piece %s on square %s can't move to square %s. Reachable squares are: %s".formatted(
                        piece.typeName().text(),
                        piece.square(),
                        targetSquare,
                        possibleSquares
                ),
                boardId,
                piece.square()
        );
    }

}
