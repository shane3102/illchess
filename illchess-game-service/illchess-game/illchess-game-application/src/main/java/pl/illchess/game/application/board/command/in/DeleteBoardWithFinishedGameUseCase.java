package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.DeleteGameWithFinishedGame;
import pl.illchess.game.domain.game.model.GameId;

import java.util.UUID;

public interface DeleteBoardWithFinishedGameUseCase {

    void deleteBoardWithFinishedGame(DeleteBoardWithFinishedGameCmd cmd);

    record DeleteBoardWithFinishedGameCmd(UUID boardId) {
        public DeleteGameWithFinishedGame toCommand() {
            return new DeleteGameWithFinishedGame(new GameId(boardId));
        }
    }
}
