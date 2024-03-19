package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.piece.model.info.PieceColor;

public class InvalidUserPerformedMoveException extends IllegalMoveException {
    public InvalidUserPerformedMoveException(
        BoardId boardId,
        Square highlightSquare,
        Username usernameOfUserPerformingMove,
        Username usernameOfUserWhichCanPerformMove,
        PieceColor movedPieceColor
    ) {
        super(
            "User %s is not authorized to perform move as %s on board with id = %s. Only user with username = %s is authorized to do that".formatted(
                usernameOfUserPerformingMove.text(),
                movedPieceColor.name(),
                boardId,
                usernameOfUserWhichCanPerformMove
            ),
            boardId,
            highlightSquare
        );
    }
}
