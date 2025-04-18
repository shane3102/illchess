package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.RejectDraw;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface RejectDrawUseCase {

    void rejectDraw(RejectDrawCmd cmd);

    record RejectDrawCmd(
        UUID boardId,
        String username
    ) {
        public RejectDraw toCommand() {
            return new RejectDraw(
                new GameId(boardId),
                new Username(username)
            );
        }
    }
}
