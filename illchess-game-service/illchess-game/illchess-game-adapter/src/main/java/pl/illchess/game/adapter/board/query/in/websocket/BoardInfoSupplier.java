package pl.illchess.game.adapter.board.query.in.websocket;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.game.application.game.query.out.ActiveGamesQueryPort;
import pl.illchess.game.application.game.query.out.GameAdditionalInfoViewQueryPort;
import pl.illchess.game.application.game.query.out.GameViewPreMoveByUserQueryPort;
import pl.illchess.game.application.game.query.out.GameViewQueryPort;
import pl.illchess.game.application.game.query.out.model.ActiveGameNewView;
import pl.illchess.game.application.game.query.out.model.GameAdditionalInfoView;
import pl.illchess.game.application.game.query.out.model.GameObtainedInfoView;
import pl.illchess.game.application.game.query.out.model.GameObtainedInfoView.GameObtainedStatus;
import pl.illchess.game.application.game.query.out.model.GameView;
import pl.illchess.game.application.game.query.out.model.GameWithPreMovesView;
import pl.illchess.game.domain.game.event.GameAdditionalInfoUpdated;
import pl.illchess.game.domain.game.event.GameStarted;
import pl.illchess.game.domain.game.event.GameUpdated;
import pl.illchess.game.domain.game.event.delete.GameDeleteInfo;
import pl.illchess.game.domain.game.event.delete.GameDeleted;
import pl.illchess.game.domain.game.event.pre_moves.GameWithPreMovesUpdated;
import pl.illchess.game.domain.game.exception.GameNotFoundException;
import pl.illchess.game.domain.game.exception.GameWithPreMovesDoesNotExistException;
import static pl.illchess.game.application.game.query.out.model.GameObtainedInfoView.GameObtainedStatus.ERROR;
import static pl.illchess.game.application.game.query.out.model.GameObtainedInfoView.GameObtainedStatus.SUCCESS;

@Service
@AllArgsConstructor
public class BoardInfoSupplier implements BoardViewSupplier {

    private static final Logger log = LoggerFactory.getLogger(BoardInfoSupplier.class);

    private final GameViewQueryPort gameViewQueryPort;
    private final GameViewPreMoveByUserQueryPort gameViewPreMoveByUserQueryPort;
    private final ActiveGamesQueryPort activeGamesQueryPort;
    private final GameAdditionalInfoViewQueryPort gameAdditionalInfoViewQueryPort;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public GameView updateBoardView(GameUpdated event) {
        log.info(
            "Update event of board with id = {} was caught, sending update of chess board view",
            event.gameId()
        );
        GameView gameView = gameViewQueryPort.findById(event.gameId().uuid())
            .orElseThrow(() -> new GameNotFoundException(event.gameId()));
        messagingTemplate.convertAndSend(
            "/chess-topic/%s".formatted(event.gameId().uuid()),
            gameView
        );
        log.info(
            "Update board view with id = {} was successfully send",
            event.gameId()
        );
        return gameView;
    }

    @Override
    public GameAdditionalInfoView updateBoardAdditionalInfoView(GameAdditionalInfoUpdated event) {
        log.info(
            "Update event of board with id = {} was caught, sending update of chess board additional info view",
            event.gameId()
        );
        GameAdditionalInfoView gameAdditionalInfoView = gameAdditionalInfoViewQueryPort.findGameById(event.gameId().uuid())
            .orElseThrow(() -> new GameNotFoundException(event.gameId()));
        messagingTemplate.convertAndSend(
            "/chess-topic/additional-info/%s".formatted(event.gameId().uuid()),
            gameAdditionalInfoView
        );
        log.info(
            "Update board additional info view with id = {} was successfully send",
            event.gameId()
        );
        return gameAdditionalInfoView;
    }

    @Override
    public GameWithPreMovesView updateBoardWithPreMovesView(GameWithPreMovesUpdated event) {
        log.info(
            "Update event of board with id = {} and pre-moves performed by username {} was cached, sending update of chess board view with pre-moves",
            event.gameId(), event.username()
        );
        GameWithPreMovesView gameWithPreMovesView = gameViewPreMoveByUserQueryPort.findByIdAndUsername(
                event.gameId().uuid(),
                event.username().text()
            )
            .orElseThrow(() -> new GameWithPreMovesDoesNotExistException(event.gameId(), event.username()));
        messagingTemplate.convertAndSend(
            "/chess-topic/%s/%s".formatted(event.gameId().uuid(), event.username().text()),
            gameWithPreMovesView
        );
        log.info(
            "Update event of board with id = {} and pre-moves performed by username {} was successfully send",
            event.gameId(), event.username()
        );
        return gameWithPreMovesView;
    }

    @Override
    public ActiveGameNewView activeBoardsChanged(GameStarted event) {
        log.info("State off active boards has changed. Sending update info with ids off active boards");
        ActiveGameNewView view = new ActiveGameNewView(event.gameId().uuid());
        messagingTemplate.convertAndSend(
            "/chess-topic/new-active-board",
            view
        );
        log.info("Successfully send info with new active boards");
        return view;
    }

    @Override
    public GameObtainedInfoView sendInfoOnGameObtained(GameDeleteInfo gameDeleteInfo) {
        GameObtainedStatus status = gameDeleteInfo instanceof GameDeleted ? SUCCESS : ERROR;
        log.info("Sending info on board delete when deletion was {}", status == SUCCESS ? "successful" : "unsuccessful");
        GameObtainedInfoView result = new GameObtainedInfoView(gameDeleteInfo.gameId().uuid(), status);
        messagingTemplate.convertAndSend("/chess-topic/obtain-status/%s".formatted(gameDeleteInfo.gameId().uuid()), result);
        messagingTemplate.convertAndSend("/chess-topic/obtain-status", result);
        log.info("Sent info on board delete when deletion was {}", status == SUCCESS ? "successful" : "unsuccessful");
        return result;
    }
}
