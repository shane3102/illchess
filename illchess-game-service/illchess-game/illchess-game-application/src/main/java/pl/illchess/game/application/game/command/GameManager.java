package pl.illchess.game.application.game.command;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.illchess.game.application.game.command.in.AcceptDrawUseCase;
import pl.illchess.game.application.game.command.in.AcceptTakingBackLastMoveUseCase;
import pl.illchess.game.application.game.command.in.CheckGameStateUseCase;
import pl.illchess.game.application.game.command.in.CheckLegalityMoveUseCase;
import pl.illchess.game.application.game.command.in.DeleteFinishedGameUseCase;
import pl.illchess.game.application.game.command.in.EstablishFenStringOfBoardUseCase;
import pl.illchess.game.application.game.command.in.JoinOrInitializeNewGameUseCase;
import pl.illchess.game.application.game.command.in.MovePieceUseCase;
import pl.illchess.game.application.game.command.in.ProposeDrawUseCase;
import pl.illchess.game.application.game.command.in.ProposeTakingBackLastMoveUseCase;
import pl.illchess.game.application.game.command.in.QuitOccupiedGameUseCase;
import pl.illchess.game.application.game.command.in.RejectDrawUseCase;
import pl.illchess.game.application.game.command.in.RejectTakingBackLastMoveUseCase;
import pl.illchess.game.application.game.command.in.ResignGameUseCase;
import pl.illchess.game.application.game.command.out.DeleteGame;
import pl.illchess.game.application.game.command.out.LoadGame;
import pl.illchess.game.application.game.command.out.SaveGame;
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

public class GameManager implements
    MovePieceUseCase,
    JoinOrInitializeNewGameUseCase,
    CheckGameStateUseCase,
    CheckLegalityMoveUseCase,
    ResignGameUseCase,
    ProposeDrawUseCase,
    RejectDrawUseCase,
    AcceptDrawUseCase,
    ProposeTakingBackLastMoveUseCase,
    RejectTakingBackLastMoveUseCase,
    AcceptTakingBackLastMoveUseCase,
    EstablishFenStringOfBoardUseCase,
    DeleteFinishedGameUseCase,
    QuitOccupiedGameUseCase {

    private static final Logger log = LoggerFactory.getLogger(GameManager.class);

    private final LoadGame loadGame;
    private final SaveGame saveGame;
    private final DeleteGame deleteGame;
    private final PublishEvent eventPublisher;

    public GameManager(LoadGame loadGame, SaveGame saveGame, DeleteGame deleteGame, PublishEvent eventPublisher) {
        this.loadGame = loadGame;
        this.saveGame = saveGame;
        this.deleteGame = deleteGame;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void movePiece(MovePieceCmd cmd) {
        log.info(
            "In game with id = {} piece is moved from square = {} to square = {}",
            cmd.gameId(),
            cmd.startSquare(),
            cmd.targetSquare()
        );
        GameId gameId = new GameId(cmd.gameId());
        Game game = loadGame.loadBoard(gameId).orElseThrow(() -> new GameNotFoundException(gameId));

        MovePiece command = cmd.toCommand();
        MoveType performedMoveType = game.movePieceOrAddPreMove(command);

        saveGame.saveBoard(game);

        log.info(
            "{} in game with id = {} was successfully performed",
            performedMoveType == MOVE ? "Move" : "Pre-move",
            cmd.gameId()
        );

        if (performedMoveType == MOVE) {
            eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(new GameId(cmd.gameId())));
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
            "In game with id = {} piece of color = {} is checked which squares can reach from square = {}",
            cmd.gameId(),
            cmd.pieceColor(),
            cmd.startSquare()
        );
        GameId gameId = new GameId(cmd.gameId());
        Game game = loadGame.loadBoard(gameId).orElseThrow(() -> new GameNotFoundException(gameId));

        CheckLegalMoves command = cmd.toCommand();
        Set<Square> legalMoves = game.legalMoves(command);
        saveGame.saveBoard(game);
        log.info(
            "In game with id = {} piece of color = {} is allowed to move from square = {} to squares = {}",
            cmd.gameId(),
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
        Optional<Game> currentPlayerBoard = loadGame.loadBoardByUsername(new Username(cmd.username()));
        if (currentPlayerBoard.isPresent()) {
            savedGameId = currentPlayerBoard.get().gameId();
            saveGame.saveBoard(currentPlayerBoard.get());
            log.info(
                "Username {} has rejoined played game",
                cmd.username()
            );
        } else {
            Optional<Game> boardWithoutPlayer = loadGame.loadBoardWithoutPlayer();
            if (boardWithoutPlayer.isPresent()) {
                savedGameId = boardWithoutPlayer.get().gameId();
                boardWithoutPlayer.get().assignSecondPlayer(command.username());
                saveGame.saveBoard(boardWithoutPlayer.get());
                eventPublisher.publishDomainEvent(new GameStarted(savedGameId));
                log.info(
                    "Username {} has joined existing game",
                    cmd.username()
                );
            } else {
                Game initializedGame = Game.generateNewBoard(command);
                savedGameId = saveGame.saveBoard(initializedGame);
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
        log.info("Checking if checkmate or stalemate in game with id = {}", cmd.gameId());
        CheckIsCheckmateOrStaleMate command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        GameState stateOfBoard = game.establishBoardState();
        saveGame.saveBoard(game);
        if (stateOfBoard != GameState.CONTINUE) {
            eventPublisher.publishDomainEvent(new GameFinished(game.gameId()));
        }
        log.info("Successfully checked state in game with id = {}. State is {}", cmd.gameId(), stateOfBoard);
    }

    @Override
    public void resignGame(ResignGameCmd cmd) {
        log.info("User {} is resigning game with id {}", cmd.username(), cmd.gameId());
        Resign command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.resign(command);
        saveGame.saveBoard(game);
        eventPublisher.publishDomainEvent(new GameFinished(command.gameId()));
        log.info("User {} successfully resigned game with id {}", cmd.username(), cmd.gameId());
    }

    @Override
    public void proposeDraw(ProposeDrawCmd cmd) {
        log.info("User {} is proposing draw in game with id = {}", cmd.username(), cmd.gameId());
        ProposeDraw command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.proposeDraw(command);
        saveGame.saveBoard(game);
        eventPublisher.publishDomainEvent(new GameStateUpdated(command.gameId()));
        log.info("User {} successfully proposed draw in game with id = {}", cmd.username(), cmd.gameId());
    }

    @Override
    public void rejectDraw(RejectDrawCmd cmd) {
        log.info("User {} is rejecting draw offer in game with id = {}", cmd.username(), cmd.gameId());
        RejectDraw command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.rejectDraw(command);
        saveGame.saveBoard(game);
        eventPublisher.publishDomainEvent(new GameStateUpdated(command.gameId()));
        log.info("User {} successfully rejected draw offer in game with id = {}", cmd.username(), cmd.gameId());
    }

    @Override
    public void acceptDraw(AcceptDrawCmd cmd) {
        log.info("User {} is accepting draw offer in game with id = {}", cmd.username(), cmd.gameId());
        AcceptDraw command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.acceptDraw(command);
        saveGame.saveBoard(game);
        eventPublisher.publishDomainEvent(new GameFinished(command.gameId()));
        log.info("User {} successfully accepted draw offer in game with id = {}", cmd.username(), cmd.gameId());
    }

    @Override
    public FenBoardString establishCurrentFenBoardString(EstablishFenStringOfBoardCmd cmd) {
        log.info("Establishing fen string of game with id = {}", cmd.gameId());
        EstablishFenStringOfBoard command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        FenBoardString result = game.establishFenBoardString();
        log.info(
            "Successfully established fen string of game with id = {}. Result is {}",
            cmd.gameId(),
            result
        );
        return result;
    }

    @Override
    public void proposeTakingBackMove(ProposeTakingBackMoveCmd cmd) {
        log.info("User {} is proposing taking back move in game with id = {}", cmd.username(), cmd.gameId());
        ProposeTakingBackMove command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.proposeTakingBackMove(command);
        saveGame.saveBoard(game);
        eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(command.gameId()));
        log.info("User {} successfully proposed taking back move in game with id = {}", cmd.username(), cmd.gameId());
    }

    @Override
    public void rejectTakingBackLastMove(RejectTakingBackMoveCmd cmd) {
        log.info("User {} is rejecting taking back last move in game with id = {}", cmd.username(), cmd.gameId());
        RejectTakingBackMove command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.rejectTakingBackLastMove(command);
        saveGame.saveBoard(game);
        eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(command.gameId()));
        log.info("User {} successfully rejected taking back last move offer in game with id = {}", cmd.username(), cmd.gameId());
    }

    @Override
    public void acceptTakingBackLastMove(AcceptTakingBackMoveCmd cmd) {
        log.info("User {} is accepting offer of taking back last move in game with id = {}", cmd.username(), cmd.gameId());
        AcceptTakingBackMove command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId())
            .orElseThrow(() -> new GameNotFoundException(command.gameId()));
        game.acceptTakingBackLastMove(command);
        saveGame.saveBoard(game);
        eventPublisher.publishDomainEvent(new GamePiecesLocationsUpdated(command.gameId()));
        log.info("User {} successfully accepted taking back last move offer in game with id = {}", cmd.username(), cmd.gameId());
    }

    @Override
    public void deleteBoardWithFinishedGame(DeleteBoardWithFinishedGameCmd cmd) {
        log.info("Deleting finished game with id = {}", cmd.gameId());
        DeleteGameWithFinishedGame command = cmd.toCommand();
        deleteGame.deleteBoard(command.gameId());
        log.info("Successfully deleted finished game with id = {}", cmd.gameId());
    }

    @Override
    public void quitOccupiedBoard(QuitOccupiedBoardCmd cmd) {
        log.info("Quiting occupied game with id: {} by username: {} ", cmd.gameId(), cmd.username());
        QuitOccupiedGame command = cmd.toCommand();
        Game game = loadGame.loadBoard(command.gameId()).orElseThrow(() -> new GameNotFoundException(command.gameId()));
        Player whitePlayer = game.gameInfo().whitePlayer();
        Player blackPlayer = game.gameInfo().blackPlayer();
        if (Objects.equals(whitePlayer.username(), command.username()) && blackPlayer == null) {
            deleteGame.deleteBoard(command.gameId());
        } else {
            throw new GameCanNotBeQuitAsAlreadyStartedException(command.gameId());
        }
        log.info("Player with username: {} successfully quit not yet started game with id: {}", cmd.username(), cmd.gameId());
    }
}
