package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.CheckLegalMoves;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.player.Username;

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
                new GameId(boardId),
                Square.valueOf(startSquare),
                new Username(username)
            );
        }
    }
}
