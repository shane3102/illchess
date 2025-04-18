package pl.illchess.game.adapter.game.command.in.rest.dto;

import java.util.UUID;

public record IllegalMoveResponse(UUID gameId, String highlightSquare, String message) {
}
