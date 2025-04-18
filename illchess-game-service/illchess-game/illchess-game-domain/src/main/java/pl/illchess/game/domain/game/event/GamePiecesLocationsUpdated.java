package pl.illchess.game.domain.game.event;

import pl.illchess.game.domain.game.model.GameId;

public record GamePiecesLocationsUpdated(GameId gameId) implements GameUpdated, GameAdditionalInfoUpdated {
}
