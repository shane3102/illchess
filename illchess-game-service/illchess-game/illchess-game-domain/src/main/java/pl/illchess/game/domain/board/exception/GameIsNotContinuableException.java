package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.state.GameState;
import pl.illchess.game.domain.commons.exception.BadRequestException;

public class GameIsNotContinuableException extends BadRequestException {
    public GameIsNotContinuableException(GameState gameState) {
        super("Game is not continuable. Game state is: %s".formatted(gameState));
    }
}
