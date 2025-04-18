package pl.illchess.game.application.board.command.in;

import java.util.UUID;
import pl.illchess.game.domain.game.command.RejectTakingBackMove;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface RejectTakingBackLastMoveUseCase {

    void rejectTakingBackLastMove(RejectTakingBackMoveCmd cmd);

    record RejectTakingBackMoveCmd(UUID boardId, String username) {
        public RejectTakingBackMove toCommand() {
            return new RejectTakingBackMove(
                new GameId(boardId),
                new Username(username)
            );
        }
    }
}
