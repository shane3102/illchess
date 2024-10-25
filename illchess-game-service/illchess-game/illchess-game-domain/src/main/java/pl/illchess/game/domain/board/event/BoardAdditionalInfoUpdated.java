package pl.illchess.game.domain.board.event;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.commons.event.DomainEvent;

public interface BoardAdditionalInfoUpdated extends DomainEvent {
    BoardId boardId();
}
