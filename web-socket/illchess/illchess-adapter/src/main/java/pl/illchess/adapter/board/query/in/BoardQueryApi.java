package pl.illchess.adapter.board.query.in;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.illchess.application.board.query.out.model.BoardView;

import java.util.UUID;

@RequestMapping("/api/board")
public interface BoardQueryApi {

    @GetMapping("/refresh/{boardId}")
    ResponseEntity<BoardView> refreshBoardView(@PathVariable UUID boardId);
}
