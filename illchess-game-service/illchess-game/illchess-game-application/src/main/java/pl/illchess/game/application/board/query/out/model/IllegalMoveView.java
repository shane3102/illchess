package pl.illchess.game.application.board.query.out.model;

import java.util.UUID;

public record IllegalMoveView(UUID boardId, String highlightSquare, String message) {
}
