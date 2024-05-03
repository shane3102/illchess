package pl.illchess.application.board.command.out;

import pl.illchess.domain.board.model.Board;
import pl.illchess.domain.board.model.BoardId;

public interface SaveBoard {

    BoardId saveBoard(Board savedBoard);
}
