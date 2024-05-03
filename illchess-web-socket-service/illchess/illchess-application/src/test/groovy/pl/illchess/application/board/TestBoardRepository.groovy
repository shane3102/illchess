package pl.illchess.application.board

import pl.illchess.application.board.command.out.LoadBoard
import pl.illchess.application.board.command.out.SaveBoard
import pl.illchess.domain.board.model.Board
import pl.illchess.domain.board.model.BoardId

class TestBoardRepository implements LoadBoard, SaveBoard {

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
}
