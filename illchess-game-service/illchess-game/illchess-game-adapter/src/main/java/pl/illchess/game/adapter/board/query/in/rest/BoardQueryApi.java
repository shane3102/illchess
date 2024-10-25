package pl.illchess.game.adapter.board.query.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.illchess.game.application.board.query.out.model.ActiveBoardsView;
import pl.illchess.game.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.game.application.board.query.out.model.BoardView;
import pl.illchess.game.application.board.query.out.model.BoardWithPreMovesView;

import java.util.UUID;

@RequestMapping("/api/board")
public interface BoardQueryApi {

    @GetMapping("/refresh/{boardId}")
    ResponseEntity<BoardView> refreshBoardView(@PathVariable UUID boardId);

    @GetMapping("/refresh/pre-moves/{boardId}/{username}")
    ResponseEntity<BoardWithPreMovesView> refreshBoardWithPreMovesView(@PathVariable UUID boardId, @PathVariable String username);

    @GetMapping("/refresh/info/{boardId}")
    ResponseEntity<BoardAdditionalInfoView> refreshBoardInfoView(@PathVariable UUID boardId);

    @GetMapping("/active")
    ResponseEntity<ActiveBoardsView> refreshActiveBoardsView();
}
