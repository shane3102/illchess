package pl.illchess.game.domain.board.event.delete;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.commons.event.DomainEvent;

public interface BoardDeleteInfo extends DomainEvent {
    BoardId boardId();
}
