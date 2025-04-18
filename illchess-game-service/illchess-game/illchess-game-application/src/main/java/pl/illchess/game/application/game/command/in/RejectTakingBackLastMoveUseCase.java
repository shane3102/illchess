package pl.illchess.game.application.game.command.in;

import java.util.UUID;
import pl.illchess.game.domain.game.command.RejectTakingBackMove;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface RejectTakingBackLastMoveUseCase {

    void rejectTakingBackLastMove(RejectTakingBackMoveCmd cmd);

    record RejectTakingBackMoveCmd(UUID gameId, String username) {
        public RejectTakingBackMove toCommand() {
            return new RejectTakingBackMove(
                new GameId(gameId),
                new Username(username)
            );
        }
    }
}
