package pl.illchess.game.domain.game.exception;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.player.Username;

public class InvalidUserPerformedMoveException extends IllegalMoveException {

    public InvalidUserPerformedMoveException(
        GameId gameId,
        Square highlightSquare,
        Username usernameOfUserPerformingMove,
        Username usernameOfUserWhichCanPerformMove
    ) {
        super(
            "User %s is not authorized to perform move in game with id = %s. Only user with username = %s is authorized to do that".formatted(
                usernameOfUserPerformingMove.text(),
                gameId,
                usernameOfUserWhichCanPerformMove
            ),
            gameId,
            highlightSquare
        );
    }
}
