package pl.illchess.game.adapter.game.command.in.rest.dto;

import java.util.UUID;

public record IllegalMoveResponse(UUID boardId, String highlightSquare, String message) {
}
