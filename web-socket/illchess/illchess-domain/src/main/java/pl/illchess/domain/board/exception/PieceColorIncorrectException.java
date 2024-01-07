package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.info.PieceColor;

public class PieceColorIncorrectException extends IllegalMoveException {
    public PieceColorIncorrectException(
            BoardId boardId,
            PieceColor movedPieceColor,
            PieceColor currentPlayerColor,
            Square highlightSquare
    ) {
        super(
                "Moved piece color is incorrect, %s color piece was moved but %s should be moved"
                        .formatted(movedPieceColor, currentPlayerColor),
                boardId,
                highlightSquare
        );
    }

}
