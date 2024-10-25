package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.commons.exception.DomainException;

public abstract class IllegalMoveException extends DomainException {

    protected BoardId boardId;
    protected Square highlightSquare;

    public IllegalMoveException(String message, BoardId boardId, Square highlightSquare) {
        super(message);
        this.boardId = boardId;
        this.highlightSquare = highlightSquare;
    }

    public BoardId getBoardId() {
        return boardId;
    }

    public Square getHighlightSquare() {
        return highlightSquare;
    }

}
