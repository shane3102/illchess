package pl.illchess.game.application.game.query.out.model;

import java.util.UUID;

public record IllegalMoveView(UUID gameId, String highlightSquare, String message) {
}
