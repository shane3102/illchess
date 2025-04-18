package pl.illchess.game.adapter.board.query.in.websocket;

import org.springframework.context.event.EventListener;
import pl.illchess.game.application.board.query.out.model.ActiveBoardNewView;
import pl.illchess.game.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.game.application.board.query.out.model.BoardGameObtainedInfoView;
import pl.illchess.game.application.board.query.out.model.BoardView;
import pl.illchess.game.application.board.query.out.model.BoardWithPreMovesView;
import pl.illchess.game.domain.game.event.GameAdditionalInfoUpdated;
import pl.illchess.game.domain.game.event.GameStarted;
import pl.illchess.game.domain.game.event.GameUpdated;
import pl.illchess.game.domain.game.event.delete.GameDeleteError;
import pl.illchess.game.domain.game.event.delete.GameDeleteInfo;
import pl.illchess.game.domain.game.event.delete.GameDeleted;
import pl.illchess.game.domain.game.event.pre_moves.GameWithPreMovesUpdated;

public interface BoardViewSupplier {

    @EventListener(GameUpdated.class)
    BoardView updateBoardView(GameUpdated event);

    @EventListener(GameAdditionalInfoUpdated.class)
    BoardAdditionalInfoView updateBoardAdditionalInfoView(GameAdditionalInfoUpdated event);

    @EventListener(GameWithPreMovesUpdated.class)
    BoardWithPreMovesView updateBoardWithPreMovesView(GameWithPreMovesUpdated event);

    @EventListener(GameStarted.class)
    ActiveBoardNewView activeBoardsChanged(GameStarted event);

    @EventListener({
        GameDeleted.class,
        GameDeleteError.class
    })
    BoardGameObtainedInfoView sendInfoOnGameObtained(GameDeleteInfo gameDeleteInfo);

}
