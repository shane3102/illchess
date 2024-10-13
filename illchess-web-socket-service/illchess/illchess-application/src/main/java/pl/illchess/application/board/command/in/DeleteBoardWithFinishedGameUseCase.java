package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.DeleteBoardWithFinishedGame;
import pl.illchess.domain.board.model.BoardId;

import java.util.UUID;

public interface DeleteBoardWithFinishedGameUseCase {

    void deleteBoardWithFinishedGame(DeleteBoardWithFinishedGameCmd cmd);

    record DeleteBoardWithFinishedGameCmd(UUID boardId) {
        public DeleteBoardWithFinishedGame toCommand() {
            return new DeleteBoardWithFinishedGame(new BoardId(boardId));
        }
    }
}
