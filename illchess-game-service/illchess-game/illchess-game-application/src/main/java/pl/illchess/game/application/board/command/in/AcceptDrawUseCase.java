package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.board.command.AcceptDraw;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

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
