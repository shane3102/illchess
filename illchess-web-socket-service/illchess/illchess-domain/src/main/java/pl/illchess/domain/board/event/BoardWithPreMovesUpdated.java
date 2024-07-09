package pl.illchess.domain.board.event;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.commons.event.DomainEvent;

public interface BoardWithPreMovesUpdated extends DomainEvent {
    BoardId boardId();

    Username username();
}
