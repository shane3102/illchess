package pl.illchess.adapter.board.query.in;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.IllegalMoveViewQueryPort;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.application.board.query.out.model.IllegalMoveView;
import pl.illchess.domain.board.event.BoardUpdated;
import pl.illchess.domain.board.event.MoveIllegal;
import pl.illchess.domain.board.exception.BoardNotFoundException;

@Service
@AllArgsConstructor
public class BoardInfoSupplier implements BoardViewSupplier, IllegalMoveViewSupplier {

    private static final Logger log = LoggerFactory.getLogger(BoardInfoSupplier.class);

    private final BoardViewQueryPort boardViewQueryPort;
    private final IllegalMoveViewQueryPort illegalMoveViewQueryPort;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public BoardView updateBoardView(BoardUpdated event) {
        log.info(
                "Update event of board with id = {} was catched, sending update of chess board view",
                event.boardId()
        );
        BoardView boardView = boardViewQueryPort.findById(event.boardId())
                .orElseThrow(() -> new BoardNotFoundException(event.boardId()));
        messagingTemplate.convertAndSend("/chess-topic", boardView);
        log.info(
                "Update board view with id = {} was successfully send",
                event.boardId()
        );
        return boardView;
    }

    @Override
    public IllegalMoveView sendIllegalMoveViewInfo(MoveIllegal event) {
        log.info(
                "Illegal move event of board with id = {} was catched, sending view with info about the mistake",
                event.boardId()
        );
        IllegalMoveView illegalMoveView = illegalMoveViewQueryPort.createIllegalMoveView(event);
        messagingTemplate.convertAndSend("/illegal-move", illegalMoveView);
        log.info(
                "Illegal view with info about the mistake of board with id = {} was successfully send",
                event.boardId()
        );
        return illegalMoveView;
    }
}
