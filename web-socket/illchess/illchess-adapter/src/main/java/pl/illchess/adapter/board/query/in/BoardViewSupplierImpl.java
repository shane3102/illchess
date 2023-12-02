package pl.illchess.adapter.board.query.in;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.event.BoardUpdated;
import pl.illchess.domain.board.exception.BoardNotFoundException;

@Service
@AllArgsConstructor
public class BoardViewSupplierImpl implements BoardViewSupplier {

    private final BoardViewQueryPort queryPort;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public BoardView updateBoardView(BoardUpdated event) {
        BoardView boardView = queryPort.findById(event.boardId())
                .orElseThrow(() -> new BoardNotFoundException(event.boardId()));
        messagingTemplate.convertAndSend("/chess-topic", boardView);
        return boardView;
    }
}
