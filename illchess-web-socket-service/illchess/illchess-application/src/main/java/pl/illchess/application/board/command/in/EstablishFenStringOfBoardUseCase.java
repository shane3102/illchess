package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.command.EstablishFenStringOfBoard;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.FenBoardString;

import java.util.UUID;

public interface EstablishFenStringOfBoardUseCase {

    FenBoardString establishCurrentFenBoardString(EstablishFenStringOfBoardCmd cmd);

    record EstablishFenStringOfBoardCmd(UUID boardId) {
        public EstablishFenStringOfBoard toCommand() {
            return new EstablishFenStringOfBoard(new BoardId(boardId));
        }
    }
}
