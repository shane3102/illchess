package pl.illchess.application.board.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase;
import pl.illchess.application.board.command.in.CheckLegalityMoveUseCase;
import pl.illchess.application.board.command.in.JoinOrInitializeNewGameUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.application.board.command.in.TakeBackMoveUseCase;
import pl.illchess.application.board.command.out.LoadBoard;
import pl.illchess.application.board.command.out.SaveBoard;
import pl.illchess.application.commons.command.out.PublishEvent;
import pl.illchess.domain.board.command.CheckIsCheckmateOrStaleMate;
import pl.illchess.domain.board.command.CheckLegalMoves;
import pl.illchess.domain.board.command.JoinOrInitializeNewGame;
import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.event.BoardInitialized;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;
import pl.illchess.domain.board.event.GameFinished;
import pl.illchess.domain.board.exception.BoardNotFoundException;
import pl.illchess.domain.board.model.Board;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.state.GameState;

import java.util.Optional;
import java.util.Set;

import static pl.illchess.domain.board.model.state.GameState.CONTINUE;

public class BoardManager implements
    MovePieceUseCase,
    TakeBackMoveUseCase,
    JoinOrInitializeNewGameUseCase,
    CheckIfCheckmateOrStalemateUseCase,
    CheckLegalityMoveUseCase {

    private static final Logger log = LoggerFactory.getLogger(BoardManager.class);

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
        log.info("");
        log.info(
            "At board with id = {} piece of type = {} and color = {} is moved from square = {} to square = {}",
            cmd.boardId(),
            cmd.pieceType(),
            cmd.pieceColor(),
            cmd.startSquare(),
            cmd.targetSquare()
        );
        BoardId boardId = new BoardId(cmd.boardId());
        Board board = loadBoard.loadBoard(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        MovePiece command = cmd.toCommand();
        board.movePiece(command);

        saveBoard.saveBoard(board);

        log.info(
            "Move at board with id = {} was successfully performed",
            cmd.boardId()
        );

        eventPublisher.publishDomainEvent(new BoardPiecesLocationsUpdated(new BoardId(cmd.boardId())));
    }

    @Override
    public Set<Square> checkLegalityOfMove(MovePieceAttemptCmd cmd) {
        log.info(
            "At board with id = {} piece of color = {} is checked which squares can reach from square = {}",
            cmd.boardId(),
            cmd.pieceColor(),
            cmd.startSquare()
        );
        BoardId boardId = new BoardId(cmd.boardId());
        Board board = loadBoard.loadBoard(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        CheckLegalMoves command = cmd.toCommand();
        Set<Square> legalMoves = board.legalMoves(command);
        saveBoard.saveBoard(board);
        log.info(
            "At board with id = {} piece of color = {} is allowed to move from square = {} to squares = {}",
            cmd.boardId(),
            cmd.pieceColor(),
            cmd.startSquare(),
            legalMoves
        );
        return legalMoves;
    }

    @Override
    public void takeBackMove(TakeBackMoveCmd cmd) {
        log.info(
            "At board with id = {} last performed move is being taken back",
            cmd.boardId()
        );
        BoardId boardId = new BoardId(cmd.boardId());
        Board board = loadBoard.loadBoard(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        board.takeBackMove();

        saveBoard.saveBoard(board);

        log.info(
            "At board with id = {} last performed move was successfully taken back",
            cmd.boardId()
        );

        eventPublisher.publishDomainEvent(new BoardPiecesLocationsUpdated(new BoardId(cmd.boardId())));
    }

    @Override
    public BoardId joinOrInitializeNewGame(JoinOrInitializeNewGameCmd cmd) {

        log.info(
            "Username {} is joining or initializing new game",
            cmd.username()
        );

        JoinOrInitializeNewGame command = cmd.toCommand();
        BoardId savedBoardId;

        Optional<Board> boardWithoutPlayer = loadBoard.loadBoardWithoutPlayer();
        if (boardWithoutPlayer.isPresent()) {
            savedBoardId = boardWithoutPlayer.get().boardId();
            boardWithoutPlayer.get().assignSecondPlayer(command.username());
            saveBoard.saveBoard(boardWithoutPlayer.get());
            log.info(
                "Username {} has joined existing game",
                cmd.username()
            );
        } else {
            Board initializedBoard = Board.generateNewBoard(command);
            savedBoardId = saveBoard.saveBoard(initializedBoard);
            eventPublisher.publishDomainEvent(new BoardInitialized(savedBoardId));
            log.info(
                "Username {} initialized new game",
                cmd.username()
            );
        }

        eventPublisher.publishDomainEvent(new BoardPiecesLocationsUpdated(savedBoardId));
        return savedBoardId;
    }

    @Override
    public void checkIfCheckmateOrStalemate(CheckIsCheckmateOrStaleMateCmd cmd) {
        log.info("Checking if checkmate or stalemate on board  = {}", cmd.boardId());
        CheckIsCheckmateOrStaleMate command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        GameState stateOfBoard = board.establishBoardState();
        saveBoard.saveBoard(board);
        if (stateOfBoard != CONTINUE) {

            eventPublisher.publishDomainEvent(new GameFinished(board.boardId()));
        }
        log.info("Successfully checked state on board = {}. State is {}", cmd.boardId(), stateOfBoard);
    }

}
