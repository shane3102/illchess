package pl.illchess.application.board

import pl.illchess.application.board.command.out.LoadBoard
import pl.illchess.application.board.command.out.SaveBoard
import pl.illchess.domain.board.model.Board
import pl.illchess.domain.board.model.BoardId

class TestBoardRepository implements LoadBoard, SaveBoard{

    Map<BoardId, Board> repo = new HashMap<>()

    @Override
    Optional<Board> loadBoard(BoardId boardId) {
        return Optional.ofNullable(repo.get(boardId))
    }

    @Override
    void saveBoard(Board savedBoard) {
        repo.put(savedBoard.boardId(), savedBoard)
    }
}
