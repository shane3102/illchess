package pl.illchess.game.adapter.game.query.in.websocket;

import org.springframework.context.event.EventListener;
import pl.illchess.game.application.game.query.out.model.ActiveGameNewView;
import pl.illchess.game.application.game.query.out.model.GameAdditionalInfoView;
import pl.illchess.game.application.game.query.out.model.GameObtainedInfoView;
import pl.illchess.game.application.game.query.out.model.GameView;
import pl.illchess.game.application.game.query.out.model.GameWithPreMovesView;
import pl.illchess.game.domain.game.event.GameAdditionalInfoUpdated;
import pl.illchess.game.domain.game.event.GameStarted;
import pl.illchess.game.domain.game.event.GameUpdated;
import pl.illchess.game.domain.game.event.delete.GameDeleteError;
import pl.illchess.game.domain.game.event.delete.GameDeleteInfo;
import pl.illchess.game.domain.game.event.delete.GameDeleted;
import pl.illchess.game.domain.game.event.pre_moves.GameWithPreMovesUpdated;

public interface GameViewSupplier {

    @EventListener(GameUpdated.class)
    GameView updateBoardView(GameUpdated event);

    @EventListener(GameAdditionalInfoUpdated.class)
    GameAdditionalInfoView updateBoardAdditionalInfoView(GameAdditionalInfoUpdated event);

    @EventListener(GameWithPreMovesUpdated.class)
    GameWithPreMovesView updateBoardWithPreMovesView(GameWithPreMovesUpdated event);

    @EventListener(GameStarted.class)
    ActiveGameNewView activeBoardsChanged(GameStarted event);

    @EventListener({
        GameDeleted.class,
        GameDeleteError.class
    })
    GameObtainedInfoView sendInfoOnGameObtained(GameDeleteInfo gameDeleteInfo);

}
