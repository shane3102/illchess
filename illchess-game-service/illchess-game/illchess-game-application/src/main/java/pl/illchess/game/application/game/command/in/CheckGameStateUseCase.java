package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.CheckIsCheckmateOrStaleMate;
import pl.illchess.game.domain.game.model.GameId;

import java.util.UUID;

public interface CheckGameStateUseCase {

    void checkBoardState(CheckBoardStateCmd cmd);

    record CheckBoardStateCmd(UUID gameId) {
        public CheckIsCheckmateOrStaleMate toCommand() {
            return new CheckIsCheckmateOrStaleMate(new GameId(gameId));
        }
    }
}
