package pl.illchess.game.application.game.command.in;

import pl.illchess.game.domain.game.command.EstablishFenStringOfBoard;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.FenBoardString;

import java.util.UUID;

public interface EstablishFenStringOfBoardUseCase {

    FenBoardString establishCurrentFenBoardString(EstablishFenStringOfBoardCmd cmd);

    record EstablishFenStringOfBoardCmd(UUID gameId) {
        public EstablishFenStringOfBoard toCommand() {
            return new EstablishFenStringOfBoard(new GameId(gameId));
        }
    }
}
