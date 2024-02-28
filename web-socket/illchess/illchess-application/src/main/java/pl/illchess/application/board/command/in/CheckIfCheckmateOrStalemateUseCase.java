package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.CheckIsCheckmateOrStaleMate;
import pl.illchess.domain.board.model.BoardId;

import java.util.UUID;

public interface CheckIfCheckmateOrStalemateUseCase {

    void checkIfCheckmateOrStalemate(CheckIsCheckmateOrStaleMateCmd cmd);

    record CheckIsCheckmateOrStaleMateCmd(UUID boardId) {
        public CheckIsCheckmateOrStaleMate toCommand() {
            return new CheckIsCheckmateOrStaleMate(new BoardId(boardId));
        }
    }
}
