package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.board.command.ProposeDraw;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

import java.util.UUID;

public interface ProposeDrawUseCase {

    void proposeDraw(ProposeDrawCmd cmd);

    record ProposeDrawCmd(UUID boardId, String username) {
        public ProposeDraw toCommand() {
            return new ProposeDraw(
                new BoardId(boardId),
                new Username(username)
            );
        }
    }
}
