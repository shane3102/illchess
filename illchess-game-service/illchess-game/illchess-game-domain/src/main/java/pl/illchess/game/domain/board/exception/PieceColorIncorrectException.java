package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.piece.model.info.PieceColor;

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
