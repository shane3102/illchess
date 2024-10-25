package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.board.command.EstablishFenStringOfBoard;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.FenBoardString;

import java.util.UUID;

public interface EstablishFenStringOfBoardUseCase {

    FenBoardString establishCurrentFenBoardString(EstablishFenStringOfBoardCmd cmd);

    record EstablishFenStringOfBoardCmd(UUID boardId) {
        public EstablishFenStringOfBoard toCommand() {
            return new EstablishFenStringOfBoard(new BoardId(boardId));
        }
    }
}
