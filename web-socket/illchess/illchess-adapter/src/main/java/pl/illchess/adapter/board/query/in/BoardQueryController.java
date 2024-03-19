package pl.illchess.adapter.board.query.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.exception.BoardNotFoundException;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BoardQueryController implements BoardQueryApi {

    private final BoardViewQueryPort boardViewQueryPort;

    @Override
    public ResponseEntity<BoardView> refreshBoardView(UUID boardId) {
        BoardView responseView = boardViewQueryPort.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        return ResponseEntity.ok(responseView);
    }
}
