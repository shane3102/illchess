package pl.illchess.adapter.board.query.in;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.event.BoardUpdated;
import pl.illchess.domain.board.exception.BoardNotFoundException;

@Service
@AllArgsConstructor
public class BoardViewSupplierImpl implements BoardViewSupplier {

    private final BoardViewQueryPort queryPort;

    @Override
    public BoardView updateBoardView(BoardUpdated event) {
        return queryPort.findById(event.boardId())
                .orElseThrow(() -> new BoardNotFoundException(event.boardId()));
    }
}
