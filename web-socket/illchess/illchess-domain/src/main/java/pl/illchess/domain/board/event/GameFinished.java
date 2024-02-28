package pl.illchess.domain.board.event;

import pl.illchess.domain.board.model.BoardId;

public record GameFinished(BoardId boardId) implements BoardUpdated {
}
