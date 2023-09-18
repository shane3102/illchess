package pl.illchess.application.game_state.in;

import pl.illchess.domain.game.GameId;

public interface CreateOrJoinGameUseCase {

    GameId createOrJoinGame();
}
