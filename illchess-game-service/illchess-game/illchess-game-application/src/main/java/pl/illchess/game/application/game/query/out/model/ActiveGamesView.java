package pl.illchess.game.application.game.query.out.model;

import java.util.List;
import java.util.UUID;

public record ActiveGamesView(List<UUID> activeGameIds) {
}
