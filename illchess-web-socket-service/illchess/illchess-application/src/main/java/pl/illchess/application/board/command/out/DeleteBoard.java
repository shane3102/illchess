package pl.illchess.application.board.command.out;

import pl.illchess.domain.board.model.BoardId;

public interface DeleteBoard {
    void deleteBoard(BoardId boardId);
}
