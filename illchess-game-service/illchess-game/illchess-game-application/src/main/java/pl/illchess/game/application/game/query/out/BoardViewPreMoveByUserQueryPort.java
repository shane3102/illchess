package pl.illchess.game.application.game.query.out;

import pl.illchess.game.application.game.query.out.model.BoardWithPreMovesView;

import java.util.Optional;
import java.util.UUID;

public interface BoardViewPreMoveByUserQueryPort {
    Optional<BoardWithPreMovesView> findByIdAndUsername(UUID boardId, String username);
}
