package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.model.info.PieceColor;

public class PieceColorIncorrectException extends IllegalMoveException {
    public PieceColorIncorrectException(
            GameId gameId,
            PieceColor movedPieceColor,
            PieceColor currentPlayerColor,
            Square highlightSquare
    ) {
        super(
                "Moved piece color is incorrect, %s color piece was moved but %s should be moved"
                        .formatted(movedPieceColor, currentPlayerColor),
            gameId,
                highlightSquare
        );
    }

}
