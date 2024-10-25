package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.board.model.state.player.Username;
import pl.illchess.game.domain.piece.model.info.PieceColor;

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

    public InvalidUserPerformedMoveException(
        BoardId boardId,
        Square highlightSquare,
        Username usernameOfUserPerformingMove,
        Username usernameOfUserWhichCanPerformMove
    ) {
        super(
            "User %s is not authorized to perform move on board with id = %s. Only user with username = %s is authorized to do that".formatted(
                usernameOfUserPerformingMove.text(),
                boardId,
                usernameOfUserWhichCanPerformMove
            ),
            boardId,
            highlightSquare
        );
    }
}
