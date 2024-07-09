package pl.illchess.adapter.board.query.in.websocket;

import org.springframework.context.event.EventListener;
import pl.illchess.application.board.query.out.model.ActiveBoardsView;
import pl.illchess.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.application.board.query.out.model.BoardWithPreMovesView;
import pl.illchess.domain.board.event.BoardAdditionalInfoUpdated;
import pl.illchess.domain.board.event.BoardInitialized;
import pl.illchess.domain.board.event.BoardUpdated;
import pl.illchess.domain.board.event.BoardWithPreMovesUpdated;
import pl.illchess.domain.board.event.GameFinished;

public interface BoardViewSupplier {

    @EventListener(BoardUpdated.class)
    BoardView updateBoardView(BoardUpdated event);

    @EventListener(BoardAdditionalInfoUpdated.class)
    BoardAdditionalInfoView updateBoardAdditionalInfoView(BoardAdditionalInfoUpdated event);

    @EventListener(BoardWithPreMovesUpdated.class)
    BoardWithPreMovesView updateBoardWithPreMovesView(BoardWithPreMovesUpdated event);

    @EventListener({
        BoardInitialized.class,
        GameFinished.class
    })
    ActiveBoardsView activeBoardsChanged(BoardUpdated event);

}
