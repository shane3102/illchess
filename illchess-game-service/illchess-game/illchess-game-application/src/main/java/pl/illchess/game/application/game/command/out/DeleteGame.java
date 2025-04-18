package pl.illchess.game.application.game.command.out;

import pl.illchess.game.domain.game.model.GameId;

public interface DeleteGame {
    void deleteBoard(GameId gameId);
}
