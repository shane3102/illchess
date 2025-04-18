package pl.illchess.game.domain.game.event;

import pl.illchess.game.domain.game.model.GameId;

public record GameStarted(GameId gameId) implements GameUpdated, GameAdditionalInfoUpdated {
}
