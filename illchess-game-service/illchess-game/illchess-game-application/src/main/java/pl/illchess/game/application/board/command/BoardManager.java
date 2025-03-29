package pl.illchess.game.application.board.command;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.illchess.game.application.board.command.in.AcceptDrawUseCase;
import pl.illchess.game.application.board.command.in.AcceptTakingBackLastMoveUseCase;
import pl.illchess.game.application.board.command.in.CheckIfCheckmateOrStalemateUseCase;
import pl.illchess.game.application.board.command.in.CheckLegalityMoveUseCase;
import pl.illchess.game.application.board.command.in.DeleteBoardWithFinishedGameUseCase;
import pl.illchess.game.application.board.command.in.EstablishFenStringOfBoardUseCase;
import pl.illchess.game.application.board.command.in.JoinOrInitializeNewGameUseCase;
import pl.illchess.game.application.board.command.in.MovePieceUseCase;
import pl.illchess.game.application.board.command.in.ProposeDrawUseCase;
import pl.illchess.game.application.board.command.in.ProposeTakingBackLastMoveUseCase;
import pl.illchess.game.application.board.command.in.QuitOccupiedBoardUseCase;
import pl.illchess.game.application.board.command.in.RejectDrawUseCase;
import pl.illchess.game.application.board.command.in.RejectTakingBackLastMoveUseCase;
import pl.illchess.game.application.board.command.in.ResignGameUseCase;
import pl.illchess.game.application.board.command.out.DeleteBoard;
import pl.illchess.game.application.board.command.out.LoadBoard;
import pl.illchess.game.application.board.command.out.SaveBoard;
import pl.illchess.game.application.commons.command.out.PublishEvent;
import pl.illchess.game.domain.board.command.AcceptDraw;
import pl.illchess.game.domain.board.command.AcceptTakingBackMove;
import pl.illchess.game.domain.board.command.CheckIsCheckmateOrStaleMate;
import pl.illchess.game.domain.board.command.CheckLegalMoves;
import pl.illchess.game.domain.board.command.DeleteBoardWithFinishedGame;
import pl.illchess.game.domain.board.command.EstablishFenStringOfBoard;
import pl.illchess.game.domain.board.command.JoinOrInitializeNewGame;
import pl.illchess.game.domain.board.command.MovePiece;
import pl.illchess.game.domain.board.command.ProposeDraw;
import pl.illchess.game.domain.board.command.ProposeTakingBackMove;
import pl.illchess.game.domain.board.command.QuitOccupiedBoard;
import pl.illchess.game.domain.board.command.RejectDraw;
import pl.illchess.game.domain.board.command.RejectTakingBackMove;
import pl.illchess.game.domain.board.command.Resign;
import pl.illchess.game.domain.board.event.BoardGameStarted;
import pl.illchess.game.domain.board.event.BoardPiecesLocationsUpdated;
import pl.illchess.game.domain.board.event.BoardStateUpdated;
import pl.illchess.game.domain.board.event.GameFinished;
import pl.illchess.game.domain.board.event.pre_moves.BoardWithPreMovesOfUsernameUpdated;
import pl.illchess.game.domain.board.exception.BoardCanNotBeQuitAsAlreadyStartedException;
import pl.illchess.game.domain.board.exception.BoardNotFoundException;
import pl.illchess.game.domain.board.model.Board;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.FenBoardString;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.board.model.state.GameState;
import pl.illchess.game.domain.board.model.state.player.Player;
import pl.illchess.game.domain.board.model.state.player.Username;
import pl.illchess.game.domain.commons.model.MoveType;
import static pl.illchess.game.domain.commons.model.MoveType.MOVE;

public class BoardManager implements
    MovePieceUseCase,
    JoinOrInitializeNewGameUseCase,
    CheckIfCheckmateOrStalemateUseCase,
    CheckLegalityMoveUseCase,
    ResignGameUseCase,
    ProposeDrawUseCase,
    RejectDrawUseCase,
    AcceptDrawUseCase,
    ProposeTakingBackLastMoveUseCase,
    RejectTakingBackLastMoveUseCase,
    AcceptTakingBackLastMoveUseCase,
    EstablishFenStringOfBoardUseCase,
    DeleteBoardWithFinishedGameUseCase,
    QuitOccupiedBoardUseCase {

    private static final Logger log = LoggerFactory.getLogger(BoardManager.class);

    private final LoadBoard loadBoard;
    private final SaveBoard saveBoard;
    private final DeleteBoard deleteBoard;
    private final PublishEvent eventPublisher;

    public BoardManager(LoadBoard loadBoard, SaveBoard saveBoard, DeleteBoard deleteBoard, PublishEvent eventPublisher) {
        this.loadBoard = loadBoard;
        this.saveBoard = saveBoard;
        this.deleteBoard = deleteBoard;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void movePiece(MovePieceCmd cmd) {
        log.info("");
        log.info(
            "At board with id = {} piece is moved from square = {} to square = {}",
            cmd.boardId(),
            cmd.startSquare(),
            cmd.targetSquare()
        );
        BoardId boardId = new BoardId(cmd.boardId());
        Board board = loadBoard.loadBoard(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        MovePiece command = cmd.toCommand();
        MoveType performedMoveType = board.movePieceOrAddPreMove(command);

        saveBoard.saveBoard(board);

        log.info(
            "{} at board with id = {} was successfully performed",
            performedMoveType == MOVE ? "Move" : "Pre-move",
            cmd.boardId()
        );

        if (performedMoveType == MOVE) {
            eventPublisher.publishDomainEvent(new BoardPiecesLocationsUpdated(new BoardId(cmd.boardId())));
        }
        if (!board.boardState().whitePlayer().preMoves().isEmpty()) {
            eventPublisher.publishDomainEvent(new BoardWithPreMovesOfUsernameUpdated(boardId, board.boardState().whitePlayer().username()));
        }
        if (board.boardState().blackPlayer() != null && !board.boardState().blackPlayer().preMoves().isEmpty()) {
            eventPublisher.publishDomainEvent(new BoardWithPreMovesOfUsernameUpdated(boardId, board.boardState().blackPlayer().username()));
        }
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
    public BoardId joinOrInitializeNewGame(JoinOrInitializeNewGameCmd cmd) {

        log.info(
            "Username {} is joining or initializing new game",
            cmd.username()
        );

        JoinOrInitializeNewGame command = cmd.toCommand();
        BoardId savedBoardId;
        Optional<Board> currentPlayerBoard = loadBoard.loadBoardByUsername(new Username(cmd.username()));
        if (currentPlayerBoard.isPresent()) {
            savedBoardId = currentPlayerBoard.get().boardId();
            saveBoard.saveBoard(currentPlayerBoard.get());
            log.info(
                "Username {} has rejoined played game",
                cmd.username()
            );
        } else {
            Optional<Board> boardWithoutPlayer = loadBoard.loadBoardWithoutPlayer();
            if (boardWithoutPlayer.isPresent()) {
                savedBoardId = boardWithoutPlayer.get().boardId();
                boardWithoutPlayer.get().assignSecondPlayer(command.username());
                saveBoard.saveBoard(boardWithoutPlayer.get());
                eventPublisher.publishDomainEvent(new BoardGameStarted(savedBoardId));
                log.info(
                    "Username {} has joined existing game",
                    cmd.username()
                );
            } else {
                Board initializedBoard = Board.generateNewBoard(command);
                savedBoardId = saveBoard.saveBoard(initializedBoard);
                log.info(
                    "Username {} initialized new game",
                    cmd.username()
                );
            }
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
        if (stateOfBoard != GameState.CONTINUE) {
            eventPublisher.publishDomainEvent(new GameFinished(board.boardId()));
        }
        log.info("Successfully checked state on board = {}. State is {}", cmd.boardId(), stateOfBoard);
    }

    @Override
    public void resignGame(ResignGameCmd cmd) {
        log.info("User {} is resigning game on board with id {}", cmd.username(), cmd.boardId());
        Resign command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        board.resign(command);
        saveBoard.saveBoard(board);
        eventPublisher.publishDomainEvent(new GameFinished(command.boardId()));
        log.info("User {} successfully resigned game on board with id {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void proposeDraw(ProposeDrawCmd cmd) {
        log.info("User {} is proposing draw on board with id = {}", cmd.username(), cmd.boardId());
        ProposeDraw command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        board.proposeDraw(command);
        saveBoard.saveBoard(board);
        eventPublisher.publishDomainEvent(new BoardStateUpdated(command.boardId()));
        log.info("User {} successfully proposed draw on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void rejectDraw(RejectDrawCmd cmd) {
        log.info("User {} is rejecting draw offer on board with id = {}", cmd.username(), cmd.boardId());
        RejectDraw command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        board.rejectDraw(command);
        saveBoard.saveBoard(board);
        eventPublisher.publishDomainEvent(new BoardStateUpdated(command.boardId()));
        log.info("User {} successfully rejected draw offer on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void acceptDraw(AcceptDrawCmd cmd) {
        log.info("User {} is accepting draw offer on board with id = {}", cmd.username(), cmd.boardId());
        AcceptDraw command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        board.acceptDraw(command);
        saveBoard.saveBoard(board);
        eventPublisher.publishDomainEvent(new GameFinished(command.boardId()));
        log.info("User {} successfully accepted draw offer on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public FenBoardString establishCurrentFenBoardString(EstablishFenStringOfBoardCmd cmd) {
        log.info("Establishing fen string of board with id = {}", cmd.boardId());
        EstablishFenStringOfBoard command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        FenBoardString result = board.establishFenBoardString();
        log.info(
            "Successfully established fen string of board with id = {}. Result is {}",
            cmd.boardId(),
            result
        );
        return result;
    }

    @Override
    public void proposeTakingBackMove(ProposeTakingBackMoveCmd cmd) {
        log.info("User {} is proposing taking back move on board with id = {}", cmd.username(), cmd.boardId());
        ProposeTakingBackMove command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        board.proposeTakingBackMove(command);
        saveBoard.saveBoard(board);
        eventPublisher.publishDomainEvent(new BoardPiecesLocationsUpdated(command.boardId()));
        log.info("User {} successfully proposed taking back move on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void rejectTakingBackLastMove(RejectTakingBackMoveCmd cmd) {
        log.info("User {} is rejecting taking back last move on board with id = {}", cmd.username(), cmd.boardId());
        RejectTakingBackMove command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        board.rejectTakingBackLastMove(command);
        saveBoard.saveBoard(board);
        eventPublisher.publishDomainEvent(new BoardPiecesLocationsUpdated(command.boardId()));
        log.info("User {} successfully rejected taking back last move offer on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void acceptTakingBackLastMove(AcceptTakingBackMoveCmd cmd) {
        log.info("User {} is accepting offer of taking back last move on board with id = {}", cmd.username(), cmd.boardId());
        AcceptTakingBackMove command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId())
            .orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        board.acceptTakingBackLastMove(command);
        saveBoard.saveBoard(board);
        eventPublisher.publishDomainEvent(new BoardPiecesLocationsUpdated(command.boardId()));
        log.info("User {} successfully accepted taking back last move offer on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void deleteBoardWithFinishedGame(DeleteBoardWithFinishedGameCmd cmd) {
        log.info("Deleting board with finished game with id = {}", cmd.boardId());
        DeleteBoardWithFinishedGame command = cmd.toCommand();
        deleteBoard.deleteBoard(command.boardId());
        log.info("Successfully deleted board with finished game with id = {}", cmd.boardId());
    }

    @Override
    public void quitOccupiedBoard(QuitOccupiedBoardCmd cmd) {
        log.info("Quiting occupied board with id: {} by username: {} ", cmd.boardId(), cmd.username());
        QuitOccupiedBoard command = cmd.toCommand();
        Board board = loadBoard.loadBoard(command.boardId()).orElseThrow(() -> new BoardNotFoundException(command.boardId()));
        Player whitePlayer = board.boardState().whitePlayer();
        Player blackPlayer = board.boardState().blackPlayer();
        if (Objects.equals(whitePlayer.username(), command.username()) && blackPlayer == null) {
            deleteBoard.deleteBoard(command.boardId());
        } else {
            throw new BoardCanNotBeQuitAsAlreadyStartedException(command.boardId());
        }
        log.info("Player with username: {} successfully quit not yet started game on board with id: {}", cmd.username(), cmd.boardId());
    }
}
