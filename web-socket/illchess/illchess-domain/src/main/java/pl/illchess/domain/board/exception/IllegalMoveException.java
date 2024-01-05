package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.event.MoveIllegal;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.commons.exception.DomainException;

public abstract class IllegalMoveException extends DomainException {

    protected BoardId boardId;
    protected Square highlightSquare;

    public IllegalMoveException(String message, BoardId boardId, Square highlightSquare) {
        super(message);
        this.boardId = boardId;
        this.highlightSquare = highlightSquare;
    }

    public MoveIllegal toMoveIllegalEvent() {
        return new MoveIllegal(
                boardId.uuid(),
                highlightSquare.toString(),
                getMessage()
        );
    }

}
