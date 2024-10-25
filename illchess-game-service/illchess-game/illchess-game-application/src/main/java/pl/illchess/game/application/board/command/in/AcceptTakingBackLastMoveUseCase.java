package pl.illchess.game.application.board.command.in;

import java.util.UUID;
import pl.illchess.game.domain.board.command.AcceptTakingBackMove;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

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
