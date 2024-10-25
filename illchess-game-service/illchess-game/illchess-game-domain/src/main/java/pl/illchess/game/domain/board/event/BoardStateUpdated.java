package pl.illchess.game.domain.board.event;

import pl.illchess.game.domain.board.model.BoardId;

public record BoardStateUpdated(BoardId boardId) implements BoardAdditionalInfoUpdated {
}
