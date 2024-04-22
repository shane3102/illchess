package pl.illchess.domain.board.event;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.commons.event.DomainEvent;

public interface BoardAdditionalInfoUpdated extends DomainEvent {
    BoardId boardId();
}
