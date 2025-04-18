package pl.illchess.game.application.game.command.in;

import java.util.UUID;
import pl.illchess.game.domain.game.command.QuitOccupiedGame;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface QuitOccupiedGameUseCase {

    void quitOccupiedBoard(QuitOccupiedBoardCmd cmd);

    record QuitOccupiedBoardCmd(
        UUID gameId,
        String username
    ) {
        public QuitOccupiedGame toCommand() {
            return new QuitOccupiedGame(
                new GameId(gameId),
                new Username(username)
            );
        }
    }
}
