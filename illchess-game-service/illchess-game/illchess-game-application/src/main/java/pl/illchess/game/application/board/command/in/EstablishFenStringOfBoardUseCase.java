package pl.illchess.game.application.board.command.in;

import pl.illchess.game.domain.game.command.EstablishFenStringOfBoard;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.FenBoardString;

import java.util.UUID;

public interface EstablishFenStringOfBoardUseCase {

    FenBoardString establishCurrentFenBoardString(EstablishFenStringOfBoardCmd cmd);

    record EstablishFenStringOfBoardCmd(UUID boardId) {
        public EstablishFenStringOfBoard toCommand() {
            return new EstablishFenStringOfBoard(new GameId(boardId));
        }
    }
}
