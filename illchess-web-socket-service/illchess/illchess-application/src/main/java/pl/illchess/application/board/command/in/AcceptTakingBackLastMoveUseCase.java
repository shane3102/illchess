package pl.illchess.application.board.command.in;

import java.util.UUID;
import pl.illchess.domain.board.command.AcceptTakingBackMove;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

public interface AcceptTakingBackLastMoveUseCase {

    void acceptTakingBackLastMove(AcceptTakingBackMoveCmd cmd);

    record AcceptTakingBackMoveCmd(UUID boardId, String username) {
        public AcceptTakingBackMove toCommand() {
            return new AcceptTakingBackMove(
                new BoardId(boardId),
                new Username(username)
            );
        }
    }
}
