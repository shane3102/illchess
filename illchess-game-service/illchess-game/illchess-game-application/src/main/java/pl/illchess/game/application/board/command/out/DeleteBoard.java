package pl.illchess.game.application.board.command.out;

import pl.illchess.game.domain.game.model.GameId;

public interface DeleteBoard {
    void deleteBoard(GameId gameId);
}
