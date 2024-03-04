package pl.illchess.adapter.board.command.in.websocket.dto;

import java.util.UUID;

public record IllegalMoveResponse(UUID boardId, String highlightSquare, String message) {
}
