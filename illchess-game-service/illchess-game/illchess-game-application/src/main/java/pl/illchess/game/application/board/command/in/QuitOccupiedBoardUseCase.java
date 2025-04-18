package pl.illchess.game.application.board.command.in;

import java.util.UUID;
import pl.illchess.game.domain.game.command.QuitOccupiedGame;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface QuitOccupiedBoardUseCase {

    void quitOccupiedBoard(QuitOccupiedBoardCmd cmd);

    record QuitOccupiedBoardCmd(
        UUID boardId,
        String username
    ) {
        public QuitOccupiedGame toCommand() {
            return new QuitOccupiedGame(
                new GameId(boardId),
                new Username(username)
            );
        }
    }
}
