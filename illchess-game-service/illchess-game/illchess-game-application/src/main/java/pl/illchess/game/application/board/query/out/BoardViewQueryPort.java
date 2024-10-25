package pl.illchess.game.application.board.query.out;

import pl.illchess.game.application.board.query.out.model.BoardView;

import java.util.Optional;
import java.util.UUID;

public interface BoardViewQueryPort {

    Optional<BoardView> findById(UUID boardId);
}
