package pl.illchess.game.domain.game.event;

import pl.illchess.game.domain.game.model.GameId;

public record GameFinished(GameId gameId) implements GameUpdated, GameAdditionalInfoUpdated {
}
