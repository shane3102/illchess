package pl.illchess.game.domain.board.event;

import pl.illchess.game.domain.board.model.BoardId;

public record BoardInitialized(BoardId boardId) implements BoardUpdated, BoardAdditionalInfoUpdated {
}
