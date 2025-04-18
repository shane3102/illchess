package pl.illchess.game.application.game.query.out;

import pl.illchess.game.application.game.query.out.model.GameView;

import java.util.Optional;
import java.util.UUID;

public interface GameViewQueryPort {

    Optional<GameView> findById(UUID gameId);
}
