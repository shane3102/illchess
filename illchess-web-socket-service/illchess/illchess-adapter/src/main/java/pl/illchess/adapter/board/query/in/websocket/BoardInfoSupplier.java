package pl.illchess.adapter.board.query.in.websocket;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.application.board.query.out.ActiveBoardsQueryPort;
import pl.illchess.application.board.query.out.BoardAdditionalInfoViewQueryPort;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.model.ActiveBoardsView;
import pl.illchess.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.event.BoardAdditionalInfoUpdated;
import pl.illchess.domain.board.event.BoardUpdated;
import pl.illchess.domain.board.exception.BoardNotFoundException;

@Service
@AllArgsConstructor
public class BoardInfoSupplier implements BoardViewSupplier {

    private static final Logger log = LoggerFactory.getLogger(BoardInfoSupplier.class);

    private final BoardViewQueryPort boardViewQueryPort;
    private final ActiveBoardsQueryPort activeBoardsQueryPort;
    private final BoardAdditionalInfoViewQueryPort boardAdditionalInfoViewQueryPort;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public BoardView updateBoardView(BoardUpdated event) {
        log.info(
            "Update event of board with id = {} was catched, sending update of chess board view",
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
            "Update event of board with id = {} was catched, sending update of chess board additional info view",
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
    public ActiveBoardsView activeBoardsChanged(BoardUpdated event) {
        log.info("State off active boards has changed. Sending update info with ids off active boards");
        ActiveBoardsView activeBoards = activeBoardsQueryPort.activeBoards();
        messagingTemplate.convertAndSend(
            "/chess-topic/active-boards",
            activeBoards
        );
        log.info("Successfully send info with new active boards");
        return activeBoards;
    }

}
