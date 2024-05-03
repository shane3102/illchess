package pl.illchess.adapter.board.query.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.illchess.application.board.query.out.ActiveBoardsQueryPort;
import pl.illchess.application.board.query.out.BoardAdditionalInfoViewQueryPort;
import pl.illchess.application.board.query.out.BoardViewQueryPort;
import pl.illchess.application.board.query.out.model.ActiveBoardsView;
import pl.illchess.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.exception.BoardNotFoundException;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BoardQueryController implements BoardQueryApi {

    private final BoardViewQueryPort boardViewQueryPort;
    private final ActiveBoardsQueryPort activeBoardsQueryPort;
    private final BoardAdditionalInfoViewQueryPort boardAdditionalInfoViewQueryPort;

    @Override
    public ResponseEntity<BoardView> refreshBoardView(UUID boardId) {
        BoardView responseView = boardViewQueryPort.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        return ResponseEntity.ok(responseView);
    }

    @Override
    public ResponseEntity<BoardAdditionalInfoView> refreshBoardInfoView(UUID boardId) {
        BoardAdditionalInfoView responseView = boardAdditionalInfoViewQueryPort.findBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        return ResponseEntity.ok(responseView);
    }

    @Override
    public ResponseEntity<ActiveBoardsView> refreshActiveBoardsView() {
        ActiveBoardsView response = activeBoardsQueryPort.activeBoards();
        return ResponseEntity.ok(response);
    }
}
