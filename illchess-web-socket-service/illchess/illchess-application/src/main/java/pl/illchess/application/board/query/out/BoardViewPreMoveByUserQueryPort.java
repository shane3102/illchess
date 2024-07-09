package pl.illchess.application.board.query.out;

import pl.illchess.application.board.query.out.model.BoardWithPreMovesView;

import java.util.Optional;
import java.util.UUID;

public interface BoardViewPreMoveByUserQueryPort {
    Optional<BoardWithPreMovesView> findByIdAndUsername(UUID boardId, String username);
}
