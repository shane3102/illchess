package pl.illchess.game.adapter.board.query.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.illchess.game.application.board.query.out.ActiveBoardsQueryPort;
import pl.illchess.game.application.board.query.out.BoardAdditionalInfoViewQueryPort;
import pl.illchess.game.application.board.query.out.BoardViewPreMoveByUserQueryPort;
import pl.illchess.game.application.board.query.out.BoardViewQueryPort;
import pl.illchess.game.application.board.query.out.model.ActiveBoardsView;
import pl.illchess.game.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.game.application.board.query.out.model.BoardView;
import pl.illchess.game.application.board.query.out.model.BoardWithPreMovesView;
import pl.illchess.game.domain.board.exception.BoardNotFoundException;
import pl.illchess.game.domain.board.exception.BoardWithPreMovesDoesNotExistException;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.state.player.Username;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BoardQueryController implements BoardQueryApi {

    private final BoardViewQueryPort boardViewQueryPort;
    private final BoardViewPreMoveByUserQueryPort boardViewPreMoveByUserQueryPort;
    private final ActiveBoardsQueryPort activeBoardsQueryPort;
    private final BoardAdditionalInfoViewQueryPort boardAdditionalInfoViewQueryPort;

    @Override
    public ResponseEntity<BoardView> refreshBoardView(UUID boardId) {
        BoardView responseView = boardViewQueryPort.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        return ResponseEntity.ok(responseView);
    }

    @Override
    public ResponseEntity<BoardWithPreMovesView> refreshBoardWithPreMovesView(UUID boardId, String username) {
        BoardWithPreMovesView responseView = boardViewPreMoveByUserQueryPort.findByIdAndUsername(boardId, username)
            .orElseThrow(() -> new BoardWithPreMovesDoesNotExistException(new BoardId(boardId), new Username(username)));
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
