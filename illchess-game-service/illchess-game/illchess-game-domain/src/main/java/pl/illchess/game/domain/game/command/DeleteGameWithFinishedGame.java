package pl.illchess.game.domain.game.command;

import pl.illchess.game.domain.game.model.GameId;

public record DeleteGameWithFinishedGame(GameId gameId) {
}
