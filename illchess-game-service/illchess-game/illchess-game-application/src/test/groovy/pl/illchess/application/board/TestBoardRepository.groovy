package pl.illchess.application.board

import pl.illchess.game.application.board.command.out.DeleteBoard
import pl.illchess.game.application.board.command.out.LoadBoard
import pl.illchess.game.application.board.command.out.SaveBoard
import pl.illchess.game.domain.board.model.Board
import pl.illchess.game.domain.board.model.BoardId

class TestBoardRepository implements LoadBoard, SaveBoard, DeleteBoard {

    Map<BoardId, Board> repo = new HashMap<>()

    @Override
    Optional<Board> loadBoard(BoardId boardId) {
        return Optional.ofNullable(repo.get(boardId))
    }

    @Override
    Optional<Board> loadBoardWithoutPlayer() {
        return repo.values()
            .stream()
            .filter {it.boardState().blackPlayer() == null}
            .findFirst()
    }

    @Override
    BoardId saveBoard(Board savedBoard) {
        def boardId = savedBoard.boardId() == null ? new BoardId(UUID.randomUUID()) : savedBoard.boardId()
        repo.put(boardId, savedBoard)
        boardId
    }

    @Override
    void deleteBoard(BoardId boardId) {
        repo.remove(boardId)
    }
}
