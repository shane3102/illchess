package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.DeleteGameWithFinishedGame;
import pl.illchess.game.domain.game.model.GameId;

import java.util.UUID;

public interface DeleteFinishedGameUseCase {

    void deleteBoardWithFinishedGame(DeleteBoardWithFinishedGameCmd cmd);

    record DeleteBoardWithFinishedGameCmd(UUID gameId) {
        public DeleteGameWithFinishedGame toCommand() {
            return new DeleteGameWithFinishedGame(new GameId(gameId));
        }
    }
}
