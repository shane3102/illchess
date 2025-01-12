package pl.illchess.game.application.board.command.in;

import java.util.UUID;
import pl.illchess.game.domain.board.command.QuitOccupiedBoard;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

public interface QuitOccupiedBoardUseCase {

    void quitOccupiedBoard(QuitOccupiedBoardCmd cmd);

    record QuitOccupiedBoardCmd(
        UUID boardId,
        String username
    ) {
        public QuitOccupiedBoard toCommand() {
            return new QuitOccupiedBoard(
                new BoardId(boardId),
                new Username(username)
            );
        }
    }
}
