package pl.illchess.game.application.board.query.out;

import java.util.Optional;
import java.util.UUID;
import pl.illchess.game.application.board.query.out.model.BoardGameFinishedView;

public interface BoardGameFinishedQueryPort {
    Optional<BoardGameFinishedView> findById(UUID boardId);
}
