package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.Resign;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface ResignGameUseCase {

    void resignGame(ResignGameCmd cmd);

    record ResignGameCmd(
        UUID gameId,
        String username
    ) {
        public Resign toCommand() {
            return new Resign(
                new GameId(gameId),
                new Username(username)
            );
        }
    }
}
