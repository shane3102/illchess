package pl.illchess.game.application.game.query.out;

import pl.illchess.game.application.game.query.out.model.ActiveGamesView;

public interface ActiveGamesQueryPort {
    ActiveGamesView activeGames();
}
