package pl.illchess.game.application.game.command.out;

import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;

public interface SaveGame {

    GameId saveBoard(Game savedGame);
}
