package pl.illchess.application.board.command.in;

import java.util.UUID;
import pl.illchess.domain.board.command.RejectTakingBackMove;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

public interface RejectTakingBackLastMoveUseCase {

    void rejectTakingBackLastMove(RejectTakingBackMoveCmd cmd);

    record RejectTakingBackMoveCmd(UUID boardId, String username) {
        public RejectTakingBackMove toCommand() {
            return new RejectTakingBackMove(
                new BoardId(boardId),
                new Username(username)
            );
        }
    }
}
