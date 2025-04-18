package pl.illchess.game.application.game.command.out;

import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;

import java.util.Optional;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface LoadGame {
    Optional<Game> loadBoard(GameId gameId);

    Optional<Game> loadBoardWithoutPlayer();

    Optional<Game> loadBoardByUsername(Username username);
}
