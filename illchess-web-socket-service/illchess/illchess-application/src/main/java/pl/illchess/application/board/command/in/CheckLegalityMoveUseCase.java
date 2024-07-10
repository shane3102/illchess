package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.CheckLegalMoves;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.state.player.Username;

import java.util.Set;
import java.util.UUID;

public interface CheckLegalityMoveUseCase {

    Set<Square> checkLegalityOfMove(MovePieceAttemptCmd cmd);

    record MovePieceAttemptCmd(
        UUID boardId,
        String startSquare,
        String pieceColor,
        String username
    ) {
        public CheckLegalMoves toCommand() {
            return new CheckLegalMoves(
                new BoardId(boardId),
                Square.valueOf(startSquare),
                new Username(username)
            );
        }
    }
}
