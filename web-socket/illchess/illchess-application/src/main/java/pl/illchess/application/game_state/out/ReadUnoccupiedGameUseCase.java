package pl.illchess.application.game_state.out;

import pl.illchess.domain.game.GameId;

public interface ReadUnoccupiedGameUseCase {

    GameId readUnoccupiedGame();
}
