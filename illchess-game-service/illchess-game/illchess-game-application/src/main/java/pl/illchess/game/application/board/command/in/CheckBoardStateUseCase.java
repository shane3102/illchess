package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.CheckIsCheckmateOrStaleMate;
import pl.illchess.game.domain.game.model.GameId;

import java.util.UUID;

public interface CheckBoardStateUseCase {

    void checkBoardState(CheckBoardStateCmd cmd);

    record CheckBoardStateCmd(UUID boardId) {
        public CheckIsCheckmateOrStaleMate toCommand() {
            return new CheckIsCheckmateOrStaleMate(new GameId(boardId));
        }
    }
}
