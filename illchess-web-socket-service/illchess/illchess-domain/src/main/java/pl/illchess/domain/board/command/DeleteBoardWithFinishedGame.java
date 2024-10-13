package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;

public record DeleteBoardWithFinishedGame(BoardId boardId) {
}
