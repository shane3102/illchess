package pl.illchess.game.application.game.query.out;

import pl.illchess.game.application.game.query.out.model.GameWithPreMovesView;

import java.util.Optional;
import java.util.UUID;

public interface GameViewPreMoveByUserQueryPort {
    Optional<GameWithPreMovesView> findByIdAndUsername(UUID boardId, String username);
}
