package pl.illchess.domain.board.event;

import pl.illchess.domain.board.model.BoardId;

public record BoardStateUpdated(BoardId boardId) implements BoardAdditionalInfoUpdated {
}
