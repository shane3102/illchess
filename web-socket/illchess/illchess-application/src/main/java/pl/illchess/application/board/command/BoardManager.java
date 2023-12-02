package pl.illchess.application.board.command;

import pl.illchess.application.board.command.in.InitializeNewBoardUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.application.board.command.in.TakeBackMoveUseCase;
import pl.illchess.application.board.command.out.LoadBoard;
import pl.illchess.application.board.command.out.SaveBoard;
import pl.illchess.application.commons.command.in.PublishEvent;
import pl.illchess.domain.board.command.InitializeNewBoard;
import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.event.BoardUpdated;
import pl.illchess.domain.board.exception.BoardNotFoundException;
import pl.illchess.domain.board.model.Board;
import pl.illchess.domain.board.model.BoardId;

public class BoardManager implements MovePieceUseCase, TakeBackMoveUseCase, InitializeNewBoardUseCase {

    private final LoadBoard loadBoard;
    private final SaveBoard saveBoard;
    private final PublishEvent eventPublisher;

    public BoardManager(LoadBoard loadBoard, SaveBoard saveBoard, PublishEvent eventPublisher) {
        this.loadBoard = loadBoard;
        this.saveBoard = saveBoard;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void movePiece(MovePieceCmd cmd) {
        BoardId boardId = new BoardId(cmd.boardId());
        Board board = loadBoard.loadBoard(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        MovePiece command = cmd.toCommand();
        board.movePiece(command);

        saveBoard.saveBoard(board);

        eventPublisher.publishDomainEvent(new BoardUpdated(cmd.boardId()));
    }

    @Override
    public void takeBackMove(TakeBackMoveCmd cmd) {
        BoardId boardId = new BoardId(cmd.boardId());
        Board board = loadBoard.loadBoard(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        board.takeBackMove();

        saveBoard.saveBoard(board);

        eventPublisher.publishDomainEvent(new BoardUpdated(cmd.boardId()));
    }

    @Override
    public void initializeNewGame(InitializeNewBoardCmd cmd) {

        InitializeNewBoard command = cmd.toCommand();

        Board initializedBoard = Board.generateNewBoard(command);
        saveBoard.saveBoard(initializedBoard);

        eventPublisher.publishDomainEvent(new BoardUpdated(command.boardId().uuid()));
    }
}
