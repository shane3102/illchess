package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;

public class TargetSquareOccupiedBySameColorPieceException extends IllegalMoveException {
    public TargetSquareOccupiedBySameColorPieceException(
            BoardId boardId,
            Square currentSquare,
            Square targetSquare
    ) {
        super(
                "Target square = %s occupied by same color piece".formatted(targetSquare),
                boardId,
                currentSquare
        );
    }
}
