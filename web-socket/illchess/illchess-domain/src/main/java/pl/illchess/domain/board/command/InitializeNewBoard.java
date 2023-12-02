package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;

public record InitializeNewBoard(BoardId boardId) {
}
