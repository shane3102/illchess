package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.board.command.CheckIsCheckmateOrStaleMate;
import pl.illchess.game.domain.board.model.BoardId;

import java.util.UUID;

public interface CheckIfCheckmateOrStalemateUseCase {

    void checkIfCheckmateOrStalemate(CheckIsCheckmateOrStaleMateCmd cmd);

    record CheckIsCheckmateOrStaleMateCmd(UUID boardId) {
        public CheckIsCheckmateOrStaleMate toCommand() {
            return new CheckIsCheckmateOrStaleMate(new BoardId(boardId));
        }
    }
}