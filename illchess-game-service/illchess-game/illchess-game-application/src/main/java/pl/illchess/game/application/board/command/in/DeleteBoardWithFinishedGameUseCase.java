package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.board.command.DeleteBoardWithFinishedGame;
import pl.illchess.game.domain.board.model.BoardId;

import java.util.UUID;

public interface DeleteBoardWithFinishedGameUseCase {

    void deleteBoardWithFinishedGame(DeleteBoardWithFinishedGameCmd cmd);

    record DeleteBoardWithFinishedGameCmd(UUID boardId) {
        public DeleteBoardWithFinishedGame toCommand() {
            return new DeleteBoardWithFinishedGame(new BoardId(boardId));
        }
    }
}
