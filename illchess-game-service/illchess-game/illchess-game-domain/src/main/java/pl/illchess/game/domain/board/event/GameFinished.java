package pl.illchess.game.domain.board.event;

import pl.illchess.game.domain.board.model.BoardId;

public record GameFinished(BoardId boardId) implements BoardUpdated, BoardAdditionalInfoUpdated {
}
