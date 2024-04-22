package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.RejectDraw;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

import java.util.UUID;

public interface RejectDrawUseCase {

    void rejectDraw(RejectDrawCmd cmd);

    record RejectDrawCmd(
        UUID boardId,
        String username
    ) {
        public RejectDraw toCommand() {
            return new RejectDraw(
                new BoardId(boardId),
                new Username(username)
            );
        }
    }
}
