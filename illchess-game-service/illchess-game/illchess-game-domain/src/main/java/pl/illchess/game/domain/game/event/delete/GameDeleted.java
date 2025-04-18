package pl.illchess.game.domain.game.event.delete;

import pl.illchess.game.domain.game.model.GameId;

public record GameDeleted(GameId gameId) implements GameDeleteInfo {
}
