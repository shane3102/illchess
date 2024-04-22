package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.AcceptDraw;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;

import java.util.UUID;

public interface AcceptDrawUseCase {

    void acceptDraw(AcceptDrawCmd cmd);

    record AcceptDrawCmd(
        UUID boardId,
        String username
    ) {
        public AcceptDraw toCommand() {
            return new AcceptDraw(
                new BoardId(boardId),
                new Username(username)
            );
        }
    }
}
