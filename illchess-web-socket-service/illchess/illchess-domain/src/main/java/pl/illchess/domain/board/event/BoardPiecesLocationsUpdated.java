package pl.illchess.domain.board.event;

import pl.illchess.domain.board.model.BoardId;

public record BoardPiecesLocationsUpdated(BoardId boardId) implements BoardUpdated, BoardAdditionalInfoUpdated {
}
