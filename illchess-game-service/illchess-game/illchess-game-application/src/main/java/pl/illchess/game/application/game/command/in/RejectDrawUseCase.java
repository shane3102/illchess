package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.RejectDraw;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface RejectDrawUseCase {

    void rejectDraw(RejectDrawCmd cmd);

    record RejectDrawCmd(
        UUID gameId,
        String username
    ) {
        public RejectDraw toCommand() {
            return new RejectDraw(
                new GameId(gameId),
                new Username(username)
            );
        }
    }
}
