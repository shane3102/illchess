package pl.illchess.game.domain.board.event.pre_moves;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;
import pl.illchess.game.domain.commons.event.DomainEvent;

public interface BoardWithPreMovesUpdated extends DomainEvent {
    BoardId boardId();

    Username username();
}
