package pl.illchess.game.domain.board.exception;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.commons.exception.DomainException;

import java.util.UUID;

public class BoardNotFoundException extends DomainException {
    public BoardNotFoundException(BoardId boardId) {
        super("Board with given id = %s not found".formatted(boardId.uuid()));
    }

    public BoardNotFoundException(UUID boardId) {
        super("Board with given id = %s not found".formatted(boardId));
    }
}
