package pl.illchess.game.domain.board.command;

import pl.illchess.game.domain.board.model.BoardId;

public record DeleteBoardWithFinishedGame(BoardId boardId) {
}
