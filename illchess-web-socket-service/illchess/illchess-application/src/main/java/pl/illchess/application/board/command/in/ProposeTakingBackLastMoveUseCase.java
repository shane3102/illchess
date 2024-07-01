package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.ProposeTakingBackMove;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

import java.util.UUID;

public interface ProposeTakingBackLastMoveUseCase {

    void proposeTakingBackMove(ProposeTakingBackMoveCmd cmd);

    record ProposeTakingBackMoveCmd(UUID boardId, String username) {
        public ProposeTakingBackMove toCommand() {
            return new ProposeTakingBackMove(
                new BoardId(boardId),
                new Username(username)
            );
        }
    }
}
