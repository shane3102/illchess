package pl.illchess.adapter.board.query.in;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.event.BoardUpdated;
import pl.illchess.domain.board.exception.BoardNotFoundException;

@Service
@AllArgsConstructor
public class BoardViewSupplierImpl implements BoardViewSupplier {

    private static final Logger log = LoggerFactory.getLogger(BoardViewSupplierImpl.class);

    private final BoardViewQueryPort queryPort;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public BoardView updateBoardView(BoardUpdated event) {
        log.info(
                "Update event of board with id = {} was catched, sending update of chess board view",
                event.boardId()
        );
        BoardView boardView = queryPort.findById(event.boardId())
                .orElseThrow(() -> new BoardNotFoundException(event.boardId()));
        messagingTemplate.convertAndSend("/chess-topic", boardView);
        log.info(
                "Update board view with id = {} was successfully send",
                event.boardId()
        );
        return boardView;
    }
}
