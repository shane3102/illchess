package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.state.GameState;
import pl.illchess.domain.commons.exception.BadRequestException;

public class GameIsNotContinuableException extends BadRequestException {
    public GameIsNotContinuableException(GameState gameState) {
        super("Game is not continuable. Game state is: %s".formatted(gameState));
    }
}
