package pl.illchess.game.domain.game.event.delete;

import pl.illchess.game.domain.game.model.GameId;

public record GameDeleteError(GameId gameId) implements GameDeleteInfo {
}
