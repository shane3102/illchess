package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.CheckLegalMoves;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.Set;
import java.util.UUID;

public interface CheckLegalityMoveUseCase {

    Set<Square> checkLegalityOfMove(MovePieceAttemptCmd cmd);

    record MovePieceAttemptCmd(
        UUID gameId,
        String startSquare,
        String pieceColor,
        String username
    ) {
        public CheckLegalMoves toCommand() {
            return new CheckLegalMoves(
                new GameId(gameId),
                Square.valueOf(startSquare),
                new Username(username)
            );
        }
    }
}
