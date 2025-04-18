package pl.illchess.game.application.game.command.in;

import java.util.UUID;
import pl.illchess.game.domain.game.command.AcceptTakingBackMove;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface AcceptTakingBackLastMoveUseCase {

    void acceptTakingBackLastMove(AcceptTakingBackMoveCmd cmd);

    record AcceptTakingBackMoveCmd(UUID gameId, String username) {
        public AcceptTakingBackMove toCommand() {
            return new AcceptTakingBackMove(
                new GameId(gameId),
                new Username(username)
            );
        }
    }
}
