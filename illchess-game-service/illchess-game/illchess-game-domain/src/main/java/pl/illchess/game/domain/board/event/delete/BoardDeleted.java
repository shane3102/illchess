package pl.illchess.game.domain.board.event.delete;

import pl.illchess.game.domain.board.model.BoardId;

public record BoardDeleted(BoardId boardId) implements BoardDeleteInfo {
}
