package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.AcceptDraw;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface AcceptDrawUseCase {

    void acceptDraw(AcceptDrawCmd cmd);

    record AcceptDrawCmd(
        UUID gameId,
        String username
    ) {
        public AcceptDraw toCommand() {
            return new AcceptDraw(
                new GameId(gameId),
                new Username(username)
            );
        }
    }
}
