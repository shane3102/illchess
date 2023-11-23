package pl.illchess.application.board.command;

import pl.illchess.application.board.command.in.InitializeNewBoard;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.application.board.command.in.TakeBackMoveUseCase;
import pl.illchess.application.board.command.out.LoadBoard;
import pl.illchess.application.board.command.out.SaveBoard;
import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.BoardNotFoundException;
import pl.illchess.domain.board.model.Board;
import pl.illchess.domain.board.model.BoardId;

import java.util.UUID;

public class PieceService implements MovePieceUseCase, TakeBackMoveUseCase, InitializeNewBoard {

    private final LoadBoard loadBoard;
    private final SaveBoard saveBoard;

    public PieceService(LoadBoard loadBoard, SaveBoard saveBoard) {
        this.loadBoard = loadBoard;
        this.saveBoard = saveBoard;
    }

    @Override
    public void movePiece(MovePieceCmd cmd) {
        BoardId boardId = new BoardId(cmd.boardId());
        Board board = loadBoard.loadBoard(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        MovePiece command = cmd.toCommand();
        board.movePiece(command);

        saveBoard.saveBoard(board);
    }

    @Override
    public void takeBackMove(TakeBackMoveCmd cmd) {
        BoardId boardId = new BoardId(cmd.boardId());
        Board board = loadBoard.loadBoard(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        board.takeBackMove();

        saveBoard.saveBoard(board);
    }

    @Override
    public BoardId initializeNewGame() {

        BoardId boardId = new BoardId(UUID.randomUUID());

        Board initializedBoard = Board.generateNewBoard(boardId);
        saveBoard.saveBoard(initializedBoard);

        return boardId;
    }
}
