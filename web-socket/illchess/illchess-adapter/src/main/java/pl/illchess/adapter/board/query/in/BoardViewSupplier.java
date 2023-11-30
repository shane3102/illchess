package pl.illchess.adapter.board.query.in;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.SendTo;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.event.BoardUpdated;

public interface BoardViewSupplier {

    @SendTo("/chess-topic/public")
    @EventListener(BoardUpdated.class)
    BoardView updateBoardView(BoardUpdated event);
}
