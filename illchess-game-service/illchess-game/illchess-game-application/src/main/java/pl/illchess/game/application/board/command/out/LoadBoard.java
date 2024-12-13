package pl.illchess.game.application.board.command.out;

import pl.illchess.game.domain.board.model.Board;
import pl.illchess.game.domain.board.model.BoardId;

import java.util.Optional;
import pl.illchess.game.domain.board.model.state.player.Username;

public interface LoadBoard {
    Optional<Board> loadBoard(BoardId boardId);

    Optional<Board> loadBoardWithoutPlayer();

    Optional<Board> loadBoardByUsername(Username username);
}
