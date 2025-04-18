package pl.illchess.game.application.board.command.out;

import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;

import java.util.Optional;
import pl.illchess.game.domain.game.model.state.player.Username;

public interface LoadBoard {
    Optional<Game> loadBoard(GameId gameId);

    Optional<Game> loadBoardWithoutPlayer();

    Optional<Game> loadBoardByUsername(Username username);
}
