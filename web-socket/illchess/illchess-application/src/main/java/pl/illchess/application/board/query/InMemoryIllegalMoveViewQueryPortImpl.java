package pl.illchess.application.board.query;

import pl.illchess.application.board.query.out.IllegalMoveViewQueryPort;
import pl.illchess.application.board.query.out.model.IllegalMoveView;
import pl.illchess.domain.board.event.MoveIllegal;

public class InMemoryIllegalMoveViewQueryPortImpl implements IllegalMoveViewQueryPort {
    @Override
    public IllegalMoveView createIllegalMoveView(MoveIllegal moveIllegal) {
        return new IllegalMoveView(
                moveIllegal.boardId(),
                moveIllegal.highlightSquare(),
                moveIllegal.message()
        );
    }
}
