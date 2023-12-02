package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.InitializeNewBoard;
import pl.illchess.domain.board.model.BoardId;

import java.util.UUID;

public interface InitializeNewBoardUseCase {

    void initializeNewGame(InitializeNewBoardCmd initializeNewBoardCmd);

    record InitializeNewBoardCmd(
            UUID newBoardId
    ) {

        public InitializeNewBoard toCommand() {
            return new InitializeNewBoard(new BoardId(newBoardId));
        }
    }
}
