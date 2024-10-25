package pl.illchess.game.application.board.command.out;

import pl.illchess.game.domain.board.model.Board;
import pl.illchess.game.domain.board.model.BoardId;

public interface SaveBoard {

    BoardId saveBoard(Board savedBoard);
}
