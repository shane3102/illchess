package pl.illchess.game.application.game.query.out;

import pl.illchess.game.application.game.query.out.model.BoardView;

import java.util.Optional;
import java.util.UUID;

public interface BoardViewQueryPort {

    Optional<BoardView> findById(UUID boardId);
}
