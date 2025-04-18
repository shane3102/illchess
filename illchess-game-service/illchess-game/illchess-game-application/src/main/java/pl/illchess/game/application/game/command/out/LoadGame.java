package pl.illchess.game.application.game.command.out;

import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;

import java.util.Optional;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface LoadGame {
    Optional<Game> loadGame(GameId gameId);

    Optional<Game> loadGameWithoutPlayer();

    Optional<Game> loadGameByUsername(Username username);
}
