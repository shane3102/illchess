package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.Resign;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

import java.util.UUID;

public interface ResignGameUseCase {

    void resignGame(ResignGameCmd cmd);

    record ResignGameCmd(
        UUID boardId,
        String username
    ) {
        public Resign toCommand() {
            return new Resign(
                new GameId(boardId),
                new Username(username)
            );
        }
    }
}
