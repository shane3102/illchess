package pl.illchess.domain.board.event;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.commons.event.DomainEvent;

public interface BoardUpdated extends DomainEvent {
    BoardId boardId();
}
