package pl.illchess.domain.board.event;

import pl.illchess.domain.board.model.BoardId;

public record BoardInitialized(BoardId boardId) implements BoardUpdated {
}
