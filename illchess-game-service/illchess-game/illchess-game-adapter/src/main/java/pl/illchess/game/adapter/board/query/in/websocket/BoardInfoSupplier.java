package pl.illchess.game.adapter.board.query.in.websocket;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.game.application.game.query.out.ActiveBoardsQueryPort;
import pl.illchess.game.application.game.query.out.BoardAdditionalInfoViewQueryPort;
import pl.illchess.game.application.game.query.out.BoardViewPreMoveByUserQueryPort;
import pl.illchess.game.application.game.query.out.BoardViewQueryPort;
import pl.illchess.game.application.game.query.out.model.ActiveBoardNewView;
import pl.illchess.game.application.game.query.out.model.BoardAdditionalInfoView;
import pl.illchess.game.application.game.query.out.model.BoardGameObtainedInfoView;
import pl.illchess.game.application.game.query.out.model.BoardGameObtainedInfoView.BoardGameObtainedStatus;
import pl.illchess.game.application.game.query.out.model.BoardView;
import pl.illchess.game.application.game.query.out.model.BoardWithPreMovesView;
import pl.illchess.game.domain.game.event.GameAdditionalInfoUpdated;
import pl.illchess.game.domain.game.event.GameStarted;
import pl.illchess.game.domain.game.event.GameUpdated;
import pl.illchess.game.domain.game.event.delete.GameDeleteInfo;
import pl.illchess.game.domain.game.event.delete.GameDeleted;
import pl.illchess.game.domain.game.event.pre_moves.GameWithPreMovesUpdated;
import pl.illchess.game.domain.game.exception.GameNotFoundException;
import pl.illchess.game.domain.game.exception.GameWithPreMovesDoesNotExistException;
import static pl.illchess.game.application.game.query.out.model.BoardGameObtainedInfoView.BoardGameObtainedStatus.ERROR;
import static pl.illchess.game.application.game.query.out.model.BoardGameObtainedInfoView.BoardGameObtainedStatus.SUCCESS;

@Service
@AllArgsConstructor
public class BoardInfoSupplier implements BoardViewSupplier {

    private static final Logger log = LoggerFactory.getLogger(BoardInfoSupplier.class);

    private final BoardViewQueryPort boardViewQueryPort;
    private final BoardViewPreMoveByUserQueryPort boardViewPreMoveByUserQueryPort;
    private final ActiveBoardsQueryPort activeBoardsQueryPort;
    private final BoardAdditionalInfoViewQueryPort boardAdditionalInfoViewQueryPort;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public BoardView updateBoardView(GameUpdated event) {
        log.info(
            "Update event of board with id = {} was caught, sending update of chess board view",
            event.gameId()
        );
        BoardView boardView = boardViewQueryPort.findById(event.gameId().uuid())
            .orElseThrow(() -> new GameNotFoundException(event.gameId()));
        messagingTemplate.convertAndSend(
            "/chess-topic/%s".formatted(event.gameId().uuid()),
            boardView
        );
        log.info(
            "Update board view with id = {} was successfully send",
            event.gameId()
        );
        return boardView;
    }

    @Override
    public BoardAdditionalInfoView updateBoardAdditionalInfoView(GameAdditionalInfoUpdated event) {
        log.info(
            "Update event of board with id = {} was caught, sending update of chess board additional info view",
            event.gameId()
        );
        BoardAdditionalInfoView boardAdditionalInfoView = boardAdditionalInfoViewQueryPort.findBoardById(event.gameId().uuid())
            .orElseThrow(() -> new GameNotFoundException(event.gameId()));
        messagingTemplate.convertAndSend(
            "/chess-topic/additional-info/%s".formatted(event.gameId().uuid()),
            boardAdditionalInfoView
        );
        log.info(
            "Update board additional info view with id = {} was successfully send",
            event.gameId()
        );
        return boardAdditionalInfoView;
    }

    @Override
    public BoardWithPreMovesView updateBoardWithPreMovesView(GameWithPreMovesUpdated event) {
        log.info(
            "Update event of board with id = {} and pre-moves performed by username {} was cached, sending update of chess board view with pre-moves",
            event.gameId(), event.username()
        );
        BoardWithPreMovesView boardWithPreMovesView = boardViewPreMoveByUserQueryPort.findByIdAndUsername(
                event.gameId().uuid(),
                event.username().text()
            )
            .orElseThrow(() -> new GameWithPreMovesDoesNotExistException(event.gameId(), event.username()));
        messagingTemplate.convertAndSend(
            "/chess-topic/%s/%s".formatted(event.gameId().uuid(), event.username().text()),
            boardWithPreMovesView
        );
        log.info(
            "Update event of board with id = {} and pre-moves performed by username {} was successfully send",
            event.gameId(), event.username()
        );
        return boardWithPreMovesView;
    }

    @Override
    public ActiveBoardNewView activeBoardsChanged(GameStarted event) {
        log.info("State off active boards has changed. Sending update info with ids off active boards");
        ActiveBoardNewView view = new ActiveBoardNewView(event.gameId().uuid());
        messagingTemplate.convertAndSend(
            "/chess-topic/new-active-board",
            view
        );
        log.info("Successfully send info with new active boards");
        return view;
    }

    @Override
    public BoardGameObtainedInfoView sendInfoOnGameObtained(GameDeleteInfo gameDeleteInfo) {
        BoardGameObtainedStatus status = gameDeleteInfo instanceof GameDeleted ? SUCCESS : ERROR;
        log.info("Sending info on board delete when deletion was {}", status == SUCCESS ? "successful" : "unsuccessful");
        BoardGameObtainedInfoView result = new BoardGameObtainedInfoView(gameDeleteInfo.gameId().uuid(), status);
        messagingTemplate.convertAndSend("/chess-topic/obtain-status/%s".formatted(gameDeleteInfo.gameId().uuid()), result);
        messagingTemplate.convertAndSend("/chess-topic/obtain-status", result);
        log.info("Sent info on board delete when deletion was {}", status == SUCCESS ? "successful" : "unsuccessful");
        return result;
    }
}
