package pl.illchess.adapter.board.query.in;

import org.springframework.context.event.EventListener;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;
import pl.illchess.domain.board.event.BoardUpdated;
import pl.illchess.domain.board.event.GameFinished;

public interface BoardViewSupplier {

    @EventListener(BoardUpdated.class)
    BoardView updateBoardView(BoardUpdated event);
}
