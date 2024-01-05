package pl.illchess.application.board.query.out;

import pl.illchess.application.board.query.out.model.IllegalMoveView;
import pl.illchess.domain.board.event.MoveIllegal;

public interface IllegalMoveViewQueryPort {

    IllegalMoveView createIllegalMoveView(MoveIllegal moveIllegal);
}
