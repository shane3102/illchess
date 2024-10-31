package pl.illchess.game.adapter.board.query.in.websocket;

import org.springframework.context.event.EventListener;
import pl.illchess.game.application.board.query.out.model.ActiveBoardNewView;
import pl.illchess.game.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.game.application.board.query.out.model.BoardGameObtainedInfoView;
import pl.illchess.game.application.board.query.out.model.BoardView;
import pl.illchess.game.application.board.query.out.model.BoardWithPreMovesView;
import pl.illchess.game.domain.board.event.BoardAdditionalInfoUpdated;
import pl.illchess.game.domain.board.event.BoardInitialized;
import pl.illchess.game.domain.board.event.BoardUpdated;
import pl.illchess.game.domain.board.event.delete.BoardDeleteError;
import pl.illchess.game.domain.board.event.delete.BoardDeleteInfo;
import pl.illchess.game.domain.board.event.delete.BoardDeleted;
import pl.illchess.game.domain.board.event.pre_moves.BoardWithPreMovesUpdated;

public interface BoardViewSupplier {

    @EventListener(BoardUpdated.class)
    BoardView updateBoardView(BoardUpdated event);

    @EventListener(BoardAdditionalInfoUpdated.class)
    BoardAdditionalInfoView updateBoardAdditionalInfoView(BoardAdditionalInfoUpdated event);

    @EventListener(BoardWithPreMovesUpdated.class)
    BoardWithPreMovesView updateBoardWithPreMovesView(BoardWithPreMovesUpdated event);

    @EventListener(BoardInitialized.class)
    ActiveBoardNewView activeBoardsChanged(BoardInitialized event);

    @EventListener({
        BoardDeleted.class,
        BoardDeleteError.class
    })
    BoardGameObtainedInfoView sendInfoOnGameObtained(BoardDeleteInfo boardDeleteInfo);

}
