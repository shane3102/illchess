package pl.illchess.game.application.board.command;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.illchess.game.application.board.command.in.AcceptDrawUseCase;
import pl.illchess.game.application.board.command.in.AcceptTakingBackLastMoveUseCase;
import pl.illchess.game.application.board.command.in.CheckBoardStateUseCase;
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
import pl.illchess.game.domain.game.command.AcceptDraw;
import pl.illchess.game.domain.game.command.AcceptTakingBackMove;
import pl.illchess.game.domain.game.command.CheckIsCheckmateOrStaleMate;
import pl.illchess.game.domain.game.command.CheckLegalMoves;
import pl.illchess.game.domain.game.command.DeleteGameWithFinishedGame;
import pl.illchess.game.domain.game.command.EstablishFenStringOfBoard;
import pl.illchess.game.domain.game.command.JoinOrInitializeNewGame;
import pl.illchess.game.domain.game.command.MovePiece;
import pl.illchess.game.domain.game.command.ProposeDraw;
import pl.illchess.game.domain.game.command.ProposeTakingBackMove;
import pl.illchess.game.domain.game.command.QuitOccupiedGame;
import pl.illchess.game.domain.game.command.RejectDraw;
import pl.illchess.game.domain.game.command.RejectTakingBackMove;
import pl.illchess.game.domain.game.command.Resign;
import pl.illchess.game.domain.game.event.GameStarted;
import pl.illchess.game.domain.game.event.GamePiecesLocationsUpdated;
import pl.illchess.game.domain.game.event.GameStateUpdated;
import pl.illchess.game.domain.game.event.GameFinished;
import pl.illchess.game.domain.game.event.pre_moves.GameWithPreMovesOfUsernameUpdated;
import pl.illchess.game.domain.game.exception.GameCanNotBeQuitAsAlreadyStartedException;
import pl.illchess.game.domain.game.exception.GameNotFoundException;
import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.FenBoardString;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.GameState;
import pl.illchess.game.domain.game.model.state.player.Player;
import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.commons.model.MoveType;
import static pl.illchess.game.domain.commons.model.MoveType.MOVE;

public class BoardManager implements
    MovePieceUseCase,
    JoinOrInitializeNewGameUseCase,
    CheckBoardStateUseCase,
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
        GameId gameId = new GameId(cmd.boardId());
        Game game = loadBoard.loadBoard(gameId).orElseThrow(() -> new GameNotFoundException(gameId));

        MovePiece command = cmd.toCommand();
        MoveType performedMoveType = game.movePieceOrAddPreMove(command);

        saveBoard.saveBoard(game);

        log.info(
            "{} at board with id = {} was successfully performed",
            performedMoveType == MOVE ? "Move" : "Pre-move",
            cmd.boardId()
        );

        if (performedMoveType == MOVE) {
            eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(new GameId(cmd.boardId())));
        }
        if (!game.gameInfo().whitePlayer().preMoves().isEmpty()) {
            eventPublisher.publishDomainEvent(new GameWithPreMovesOfUsernameUpdated(gameId, game.gameInfo().whitePlayer().username()));
        }
        if (game.gameInfo().blackPlayer() != null && !game.gameInfo().blackPlayer().preMoves().isEmpty()) {
            eventPublisher.publishDomainEvent(new GameWithPreMovesOfUsernameUpdated(gameId, game.gameInfo().blackPlayer().username()));
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
        GameId gameId = new GameId(cmd.boardId());
        Game game = loadBoard.loadBoard(gameId).orElseThrow(() -> new GameNotFoundException(gameId));

        CheckLegalMoves command = cmd.toCommand();
        Set<Square> legalMoves = game.legalMoves(command);
        saveBoard.saveBoard(game);
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
    public GameId joinOrInitializeNewGame(JoinOrInitializeNewGameCmd cmd) {

        log.info(
            "Username {} is joining or initializing new game",
            cmd.username()
        );

        JoinOrInitializeNewGame command = cmd.toCommand();
        GameId savedGameId;
        Optional<Game> currentPlayerBoard = loadBoard.loadBoardByUsername(new Username(cmd.username()));
        if (currentPlayerBoard.isPresent()) {
            savedGameId = currentPlayerBoard.get().gameId();
            saveBoard.saveBoard(currentPlayerBoard.get());
            log.info(
                "Username {} has rejoined played game",
                cmd.username()
            );
        } else {
            Optional<Game> boardWithoutPlayer = loadBoard.loadBoardWithoutPlayer();
            if (boardWithoutPlayer.isPresent()) {
                savedGameId = boardWithoutPlayer.get().gameId();
                boardWithoutPlayer.get().assignSecondPlayer(command.username());
                saveBoard.saveBoard(boardWithoutPlayer.get());
                eventPublisher.publishDomainEvent(new GameStarted(savedGameId));
                log.info(
                    "Username {} has joined existing game",
                    cmd.username()
                );
            } else {
                Game initializedGame = Game.generateNewBoard(command);
                savedGameId = saveBoard.saveBoard(initializedGame);
                log.info(
                    "Username {} initialized new game",
                    cmd.username()
                );
            }
        }

        eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(savedGameId));
        return savedGameId;
    }

    @Override
    public void checkBoardState(CheckBoardStateCmd cmd) {
        log.info("Checking if checkmate or stalemate on board  = {}", cmd.boardId());
        CheckIsCheckmateOrStaleMate command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        GameState stateOfBoard = game.establishBoardState();
        saveBoard.saveBoard(game);
        if (stateOfBoard != GameState.CONTINUE) {
            eventPublisher.publishDomainEvent(new GameFinished(game.gameId()));
        }
        log.info("Successfully checked state on board = {}. State is {}", cmd.boardId(), stateOfBoard);
    }

    @Override
    public void resignGame(ResignGameCmd cmd) {
        log.info("User {} is resigning game on board with id {}", cmd.username(), cmd.boardId());
        Resign command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.resign(command);
        saveBoard.saveBoard(game);
        eventPublisher.publishDomainEvent(new GameFinished(command.gameId()));
        log.info("User {} successfully resigned game on board with id {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void proposeDraw(ProposeDrawCmd cmd) {
        log.info("User {} is proposing draw on board with id = {}", cmd.username(), cmd.boardId());
        ProposeDraw command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.proposeDraw(command);
        saveBoard.saveBoard(game);
        eventPublisher.publishDomainEvent(new GameStateUpdated(command.gameId()));
        log.info("User {} successfully proposed draw on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void rejectDraw(RejectDrawCmd cmd) {
        log.info("User {} is rejecting draw offer on board with id = {}", cmd.username(), cmd.boardId());
        RejectDraw command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.rejectDraw(command);
        saveBoard.saveBoard(game);
        eventPublisher.publishDomainEvent(new GameStateUpdated(command.gameId()));
        log.info("User {} successfully rejected draw offer on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void acceptDraw(AcceptDrawCmd cmd) {
        log.info("User {} is accepting draw offer on board with id = {}", cmd.username(), cmd.boardId());
        AcceptDraw command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.acceptDraw(command);
        saveBoard.saveBoard(game);
        eventPublisher.publishDomainEvent(new GameFinished(command.gameId()));
        log.info("User {} successfully accepted draw offer on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public FenBoardString establishCurrentFenBoardString(EstablishFenStringOfBoardCmd cmd) {
        log.info("Establishing fen string of board with id = {}", cmd.boardId());
        EstablishFenStringOfBoard command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        FenBoardString result = game.establishFenBoardString();
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
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.proposeTakingBackMove(command);
        saveBoard.saveBoard(game);
        eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(command.gameId()));
        log.info("User {} successfully proposed taking back move on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void rejectTakingBackLastMove(RejectTakingBackMoveCmd cmd) {
        log.info("User {} is rejecting taking back last move on board with id = {}", cmd.username(), cmd.boardId());
        RejectTakingBackMove command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.rejectTakingBackLastMove(command);
        saveBoard.saveBoard(game);
        eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(command.gameId()));
        log.info("User {} successfully rejected taking back last move offer on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void acceptTakingBackLastMove(AcceptTakingBackMoveCmd cmd) {
        log.info("User {} is accepting offer of taking back last move on board with id = {}", cmd.username(), cmd.boardId());
        AcceptTakingBackMove command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.acceptTakingBackLastMove(command);
        saveBoard.saveBoard(game);
        eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(command.gameId()));
        log.info("User {} successfully accepted taking back last move offer on board with id = {}", cmd.username(), cmd.boardId());
    }

    @Override
    public void deleteBoardWithFinishedGame(DeleteBoardWithFinishedGameCmd cmd) {
        log.info("Deleting board with finished game with id = {}", cmd.boardId());
        DeleteGameWithFinishedGame command = cmd.toCommand();
        deleteBoard.deleteBoard(command.gameId());
        log.info("Successfully deleted board with finished game with id = {}", cmd.boardId());
    }

    @Override
    public void quitOccupiedBoard(QuitOccupiedBoardCmd cmd) {
        log.info("Quiting occupied board with id: {} by username: {} ", cmd.boardId(), cmd.username());
        QuitOccupiedGame command = cmd.toCommand();
        Game game = loadBoard.loadBoard(command.gameId()).orElseThrow(() -> new GameNotFoundException(command.gameId()));
        Player whitePlayer = game.gameInfo().whitePlayer();
        Player blackPlayer = game.gameInfo().blackPlayer();
        if (Objects.equals(whitePlayer.username(), command.username()) && blackPlayer == null) {
            deleteBoard.deleteBoard(command.gameId());
        } else {
            throw new GameCanNotBeQuitAsAlreadyStartedException(command.gameId());
        }
        log.info("Player with username: {} successfully quit not yet started game on board with id: {}", cmd.username(), cmd.boardId());
    }
}
