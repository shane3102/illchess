package pl.illchess.domain.board.exception;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.commons.exception.DomainException;

import java.util.UUID;

public class BoardNotFoundException extends DomainException {
    public BoardNotFoundException(BoardId boardId) {
        super("Board with given id = %s not found".formatted(boardId.uuid()));
    }

    public BoardNotFoundException(UUID boardId) {
        super("Board with given id = %s not found".formatted(boardId));
    }
}
