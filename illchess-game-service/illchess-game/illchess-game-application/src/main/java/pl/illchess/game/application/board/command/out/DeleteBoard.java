package pl.illchess.game.application.board.command.out;

import pl.illchess.game.domain.board.model.BoardId;

public interface DeleteBoard {
    void deleteBoard(BoardId boardId);
}
