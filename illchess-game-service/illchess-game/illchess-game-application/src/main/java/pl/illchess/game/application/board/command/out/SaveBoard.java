package pl.illchess.game.application.board.command.out;

import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;

public interface SaveBoard {

    GameId saveBoard(Game savedGame);
}
