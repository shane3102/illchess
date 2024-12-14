package pl.illchess.game.adapter.board.query.in.websocket;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.game.application.board.query.out.ActiveBoardsQueryPort;
import pl.illchess.game.application.board.query.out.BoardAdditionalInfoViewQueryPort;
import pl.illchess.game.application.board.query.out.BoardViewPreMoveByUserQueryPort;
import pl.illchess.game.application.board.query.out.BoardViewQueryPort;
import pl.illchess.game.application.board.query.out.model.ActiveBoardNewView;
import pl.illchess.game.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.game.application.board.query.out.model.BoardGameObtainedInfoView;
import pl.illchess.game.application.board.query.out.model.BoardGameObtainedInfoView.BoardGameObtainedStatus;
import pl.illchess.game.application.board.query.out.model.BoardView;
import pl.illchess.game.application.board.query.out.model.BoardWithPreMovesView;
import pl.illchess.game.domain.board.event.BoardAdditionalInfoUpdated;
import pl.illchess.game.domain.board.event.BoardGameStarted;
import pl.illchess.game.domain.board.event.BoardUpdated;
import pl.illchess.game.domain.board.event.delete.BoardDeleteInfo;
import pl.illchess.game.domain.board.event.delete.BoardDeleted;
import pl.illchess.game.domain.board.event.pre_moves.BoardWithPreMovesUpdated;
import pl.illchess.game.domain.board.exception.BoardNotFoundException;
import pl.illchess.game.domain.board.exception.BoardWithPreMovesDoesNotExistException;
import static pl.illchess.game.application.board.query.out.model.BoardGameObtainedInfoView.BoardGameObtainedStatus.ERROR;
import static pl.illchess.game.application.board.query.out.model.BoardGameObtainedInfoView.BoardGameObtainedStatus.SUCCESS;

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
    public BoardView updateBoardView(BoardUpdated event) {
        log.info(
            "Update event of board with id = {} was caught, sending update of chess board view",
            event.boardId()
        );
        BoardView boardView = boardViewQueryPort.findById(event.boardId().uuid())
            .orElseThrow(() -> new BoardNotFoundException(event.boardId()));
        messagingTemplate.convertAndSend(
            "/chess-topic/%s".formatted(event.boardId().uuid()),
            boardView
        );
        log.info(
            "Update board view with id = {} was successfully send",
            event.boardId()
        );
        return boardView;
    }

    @Override
    public BoardAdditionalInfoView updateBoardAdditionalInfoView(BoardAdditionalInfoUpdated event) {
        log.info(
            "Update event of board with id = {} was caught, sending update of chess board additional info view",
            event.boardId()
        );
        BoardAdditionalInfoView boardAdditionalInfoView = boardAdditionalInfoViewQueryPort.findBoardById(event.boardId().uuid())
            .orElseThrow(() -> new BoardNotFoundException(event.boardId()));
        messagingTemplate.convertAndSend(
            "/chess-topic/additional-info/%s".formatted(event.boardId().uuid()),
            boardAdditionalInfoView
        );
        log.info(
            "Update board additional info view with id = {} was successfully send",
            event.boardId()
        );
        return boardAdditionalInfoView;
    }

    @Override
    public BoardWithPreMovesView updateBoardWithPreMovesView(BoardWithPreMovesUpdated event) {
        log.info(
            "Update event of board with id = {} and pre-moves performed by username {} was cached, sending update of chess board view with pre-moves",
            event.boardId(), event.username()
        );
        BoardWithPreMovesView boardWithPreMovesView = boardViewPreMoveByUserQueryPort.findByIdAndUsername(
                event.boardId().uuid(),
                event.username().text()
            )
            .orElseThrow(() -> new BoardWithPreMovesDoesNotExistException(event.boardId(), event.username()));
        messagingTemplate.convertAndSend(
            "/chess-topic/%s/%s".formatted(event.boardId().uuid(), event.username().text()),
            boardWithPreMovesView
        );
        log.info(
            "Update event of board with id = {} and pre-moves performed by username {} was successfully send",
            event.boardId(), event.username()
        );
        return boardWithPreMovesView;
    }

    @Override
    public ActiveBoardNewView activeBoardsChanged(BoardGameStarted event) {
        log.info("State off active boards has changed. Sending update info with ids off active boards");
        ActiveBoardNewView view = new ActiveBoardNewView(event.boardId().uuid());
        messagingTemplate.convertAndSend(
            "/chess-topic/new-active-board",
            view
        );
        log.info("Successfully send info with new active boards");
        return view;
    }

    @Override
    public BoardGameObtainedInfoView sendInfoOnGameObtained(BoardDeleteInfo boardDeleteInfo) {
        BoardGameObtainedStatus status = boardDeleteInfo instanceof BoardDeleted ? SUCCESS : ERROR;
        log.info("Sending info on board delete when deletion was {}", status == SUCCESS ? "successful" : "unsuccessful");
        BoardGameObtainedInfoView result = new BoardGameObtainedInfoView(boardDeleteInfo.boardId().uuid(), status);
        messagingTemplate.convertAndSend("/chess-topic/obtain-status/%s".formatted(boardDeleteInfo.boardId().uuid()), result);
        messagingTemplate.convertAndSend("/chess-topic/obtain-status", result);
        log.info("Sent info on board delete when deletion was {}", status == SUCCESS ? "successful" : "unsuccessful");
        return result;
    }
}
