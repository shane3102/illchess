package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.ProposeDraw;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface ProposeDrawUseCase {

    void proposeDraw(ProposeDrawCmd cmd);

    record ProposeDrawCmd(UUID gameId, String username) {
        public ProposeDraw toCommand() {
            return new ProposeDraw(
                new GameId(gameId),
                new Username(username)
            );
        }
    }
}
