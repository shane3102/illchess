package pl.illchess.game.application.game.query.out;

import java.util.Optional;
import java.util.UUID;
import pl.illchess.game.application.game.query.out.model.GameFinishedView;

public interface GameFinishedQueryPort {
    Optional<GameFinishedView> findById(UUID boardId);
}
