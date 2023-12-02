package pl.illchess.adapter.board.query.in;

import org.springframework.context.event.EventListener;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.event.BoardUpdated;

public interface BoardViewSupplier {

    @EventListener(BoardUpdated.class)
    BoardView updateBoardView(BoardUpdated event);
}
