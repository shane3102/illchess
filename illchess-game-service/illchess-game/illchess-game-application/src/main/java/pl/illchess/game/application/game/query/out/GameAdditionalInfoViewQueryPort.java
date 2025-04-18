package pl.illchess.game.application.game.query.out;

import pl.illchess.game.application.game.query.out.model.GameAdditionalInfoView;

import java.util.Optional;
import java.util.UUID;

public interface GameAdditionalInfoViewQueryPort {
    Optional<GameAdditionalInfoView> findGameById(UUID gameId);
}
