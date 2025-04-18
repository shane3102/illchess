package pl.illchess.game.domain.game.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;
import pl.illchess.game.domain.game.command.AcceptDraw;
import pl.illchess.game.domain.game.command.AcceptTakingBackMove;
import pl.illchess.game.domain.game.command.CheckLegalMoves;
import pl.illchess.game.domain.game.command.JoinOrInitializeNewGame;
import pl.illchess.game.domain.game.command.MovePiece;
import pl.illchess.game.domain.game.command.ProposeDraw;
import pl.illchess.game.domain.game.command.ProposeTakingBackMove;
import pl.illchess.game.domain.game.command.RejectDraw;
import pl.illchess.game.domain.game.command.RejectTakingBackMove;
import pl.illchess.game.domain.game.command.Resign;
import pl.illchess.game.domain.game.exception.InvalidUserPerformedMoveException;
import pl.illchess.game.domain.game.exception.NoBlackPlayerException;
import pl.illchess.game.domain.game.exception.NoMovesPerformedException;
import pl.illchess.game.domain.game.exception.PieceCantMoveToGivenSquareException;
import pl.illchess.game.domain.game.exception.PieceColorIncorrectException;
import pl.illchess.game.domain.game.exception.PieceIsAlreadyOnSquareUserTriesToPlaceItOnException;
import pl.illchess.game.domain.game.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.game.domain.game.model.history.Move;
import pl.illchess.game.domain.game.model.history.MoveHistory;
import pl.illchess.game.domain.game.model.square.Board;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.GameInfo;
import pl.illchess.game.domain.game.model.state.GameResultCause;
import pl.illchess.game.domain.game.model.state.GameState;
import pl.illchess.game.domain.game.model.state.player.Player;
import pl.illchess.game.domain.game.model.state.player.PreMove;
import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.commons.model.MoveType;
import pl.illchess.game.domain.piece.exception.KingNotFoundOnBoardException;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.type.Bishop;
import pl.illchess.game.domain.piece.model.type.King;
import pl.illchess.game.domain.piece.model.type.Knight;
import static pl.illchess.game.domain.piece.model.info.PieceColor.BLACK;
import static pl.illchess.game.domain.piece.model.info.PieceColor.WHITE;

public record Game(
    GameId gameId,
    Board board,
    MoveHistory moveHistory,
    GameInfo gameInfo
) {

    public Game(
        GameId gameId,
        FenBoardString fenString,
        MoveHistory moveHistory,
        Username username
    ) {
        this(
            gameId,
            Board.fromFENString(fenString),
            moveHistory,
            GameInfo.fromFenStringAndByUsername(fenString, username)
        );
    }

    public MoveType movePieceOrAddPreMove(MovePiece command) {
        throwIfNoBlackPlayer();
        if (gameInfo.isPreMove(command)) {
            addPreMove(command);
            return MoveType.PRE_MOVE;
        } else {
            movePiece(command);
            return MoveType.MOVE;
        }
    }

    private void movePiece(MovePiece command) {
        Piece movedPiece = board().findPieceOnSquare(command.startSquare())
            .orElseThrow(() -> new PieceNotPresentOnGivenSquare(command.gameId(), command.startSquare()));

        if (Objects.equals(movedPiece.square(), command.targetSquare())) {
            throw new PieceIsAlreadyOnSquareUserTriesToPlaceItOnException(movedPiece, command.targetSquare());
        }

        gameInfo().checkIfAllowedToMove(gameId, movedPiece, command.username());

        Set<Square> possibleMoves = movedPiece.possibleMoves(board, moveHistory);

        if (!possibleMoves.contains(command.targetSquare())) {
            throw new PieceCantMoveToGivenSquareException(
                movedPiece,
                command.targetSquare(),
                possibleMoves,
                command.gameId()
            );
        }

        FenBoardString fenBoardString = establishFenBoardString();

        Move performedMove = board().movePiece(command, movedPiece, moveHistory().peekLastMove(), fenBoardString);
        resetCachedMovesOfPieces();
        moveHistory().addMoveToHistory(performedMove);
        gameInfo().invertCurrentPlayerColor();

        refreshPreMovesOfPlayers();
    }

    private void refreshPreMovesOfPlayers() {
        Player currentPlayer = gameInfo().currentPlayer();
        if (currentPlayer != null && !currentPlayer.preMoves().isEmpty()) {
            PreMove preMove = currentPlayer.preMoves().removeFirst();
            MovePiece scheduledPreMoveCommand = preMove.toCommand(gameId, gameInfo.currentPlayer().username());
            try {
                movePieceOrAddPreMove(scheduledPreMoveCommand);
            } catch (PieceCantMoveToGivenSquareException | PieceColorIncorrectException ignored) {
                currentPlayer.preMoves().clear();
            }
            return;
        }
        Player inactivePlayer = gameInfo.inactivePlayer();
        if (inactivePlayer != null && !inactivePlayer.preMoves().isEmpty()) {
            List<MovePiece> movePiecePreMoveCommands = inactivePlayer.preMoves().stream()
                .map(preMove -> preMove.toCommand(gameId, inactivePlayer.username()))
                .toList();
            inactivePlayer.preMoves().clear();
            movePiecePreMoveCommands.forEach(this::movePieceOrAddPreMove);
        }
    }

    private void addPreMove(MovePiece command) {
        Optional<Board> lastLocationsOnPreviousPreMove = gameInfo().getLastLocationsOnPreviousPreMove(command.username());
        Board clonedBoardBeforePreMove = lastLocationsOnPreviousPreMove.orElse(board()).cloneBoard();

        Piece movedPiece = clonedBoardBeforePreMove.findPieceOnSquare(command.startSquare())
            .orElseThrow(() -> new PieceNotPresentOnGivenSquare(command.gameId(), command.startSquare()));

        Player ownerOfPiece = movedPiece.color() == WHITE ? gameInfo.whitePlayer() : gameInfo.blackPlayer();
        if (!Objects.equals(ownerOfPiece.username(), command.username())) {
            throw new InvalidUserPerformedMoveException(gameId, command.startSquare(), command.username(), ownerOfPiece.username());
        }

        Set<Square> possibleMovesOnPreMove = movedPiece.possibleMovesOnPreMove(clonedBoardBeforePreMove, moveHistory());

        if (!possibleMovesOnPreMove.contains(command.targetSquare())) {
            throw new PieceCantMoveToGivenSquareException(
                movedPiece,
                command.targetSquare(),
                possibleMovesOnPreMove,
                command.gameId()
            );
        }
        PreMove preMove = clonedBoardBeforePreMove.movePieceOnPreMove(command, movedPiece);
        Player preMovingPlayer = gameInfo.getPlayerByUsername(command.username())
            .orElseThrow(
                () -> new InvalidUserPerformedMoveException(command.gameId(), command.startSquare(), command.username(), gameInfo.currentPlayer().username())
            );
        preMovingPlayer.preMoves().addLast(preMove);

    }

    public Set<Square> legalMoves(CheckLegalMoves command) {
        throwIfNoBlackPlayer();
        Optional<Player> playerByUsername = gameInfo.getPlayerByUsername(command.username());

        if (playerByUsername.isEmpty() || playerByUsername.get().equals(gameInfo.currentPlayer())) {
            Piece movedPiece = board.findPieceOnSquare(command.square())
                .orElseThrow(() -> new PieceNotPresentOnGivenSquare(gameId, command.square()));
            if (!Objects.equals(movedPiece.color(), gameInfo().currentPlayerColor().color())) {
                throw new PieceColorIncorrectException(
                    gameId,
                    movedPiece.color(),
                    gameInfo().currentPlayerColor().color(),
                    movedPiece.square()
                );
            }
            return movedPiece.possibleMoves(board, moveHistory);
        }
        if (!playerByUsername.get().preMoves().isEmpty()) {
            Piece movedPiece = playerByUsername.get().preMoves().getLast().boardAfterPreMove().findPieceOnSquare(command.square())
                .orElseThrow(() -> new PieceNotPresentOnGivenSquare(gameId, command.square()));
            return movedPiece.possibleMovesOnPreMove(board, moveHistory);
        }
        Piece movedPiece = board.findPieceOnSquare(command.square())
            .orElseThrow(() -> new PieceNotPresentOnGivenSquare(gameId, command.square()));

        if (movedPiece.color() != gameInfo.currentPlayerColor().color() && !Objects.equals(gameInfo.currentPlayer(), playerByUsername.get())) {
            return movedPiece.possibleMovesOnPreMove(board, moveHistory);
        }

        return Set.of();
    }

    public GameState establishBoardState() {
        King king = (King) board.getPieceByTypeAndColor(King.class, gameInfo().currentPlayerColor().color())
            .orElseThrow(KingNotFoundOnBoardException::new);

        boolean kingIsAttacked = board.getEnemyPieces(gameInfo().currentPlayerColor().color())
            .stream()
            .anyMatch(piece -> piece.isAttackingSquare(king.square(), board, moveHistory.peekLastMove()));

        boolean anyPieceCanMove = board.getAlliedPieces(gameInfo().currentPlayerColor().color()).stream()
            .anyMatch(piece -> !piece.possibleMoves(board, moveHistory).isEmpty());

        GameState establishedState;
        GameResultCause establishedGameResultCause = null;

        if (kingIsAttacked && !anyPieceCanMove) {
            establishedState = king.color() == WHITE ? GameState.BLACK_WON : GameState.WHITE_WON;
            establishedGameResultCause = GameResultCause.CHECKMATE;
        } else if (!kingIsAttacked && !anyPieceCanMove) {
            establishedState = GameState.DRAW;
            establishedGameResultCause = GameResultCause.STALEMATE;
        } else if(isInsufficientMaterial()) {
            establishedState = GameState.DRAW;
            establishedGameResultCause = GameResultCause.INSUFFICIENT_MATERIAL;
        } else {
            establishedState = GameState.CONTINUE;
        }

        if (establishedState != GameState.CONTINUE) {
            gameInfo.changeState(establishedState, establishedGameResultCause);
        }

        return establishedState;
    }

    public boolean isInsufficientMaterial() {
        Supplier<Stream<Piece>> piecesLocationsSupplier = () -> board.piecesLocations().stream();

        List<Piece> whitePieces = piecesLocationsSupplier.get().filter(piece -> piece.color() == WHITE).toList();
        List<Piece> blackPieces = piecesLocationsSupplier.get().filter(piece -> piece.color() == BLACK).toList();

        boolean isWhiteInsufficientMaterial = whitePieces.size() == 1 || (whitePieces.size() == 2 && whitePieces.stream().anyMatch(piece -> piece instanceof Bishop || piece instanceof Knight));
        boolean isBlackInsufficientMaterial = blackPieces.size() == 1 || (blackPieces.size() == 2 && blackPieces.stream().anyMatch(piece -> piece instanceof Bishop || piece instanceof Knight));
        boolean insufficientMaterialSpecialCase = blackPieces.size() == 1 && (whitePieces.size() == 3 && whitePieces.stream().filter(piece -> piece instanceof Knight).count() == 2) ||
            whitePieces.size() == 1 && (blackPieces.size() == 3 && blackPieces.stream().filter(piece -> piece instanceof Knight).count() == 2);

        return isWhiteInsufficientMaterial && isBlackInsufficientMaterial || insufficientMaterialSpecialCase;
    }

    public void assignSecondPlayer(Username username) {
        gameInfo.setBlackPlayer(new Player(username));
    }

    public void resign(Resign command) {
        gameInfo.resign(command);
    }

    public void proposeDraw(ProposeDraw command) {
        gameInfo.proposeDraw(command);
    }

    public void acceptDraw(AcceptDraw command) {
        gameInfo.acceptDrawOffer(command);
    }

    public void rejectDraw(RejectDraw command) {
        gameInfo.rejectDrawOffer(command);
    }

    public void proposeTakingBackMove(ProposeTakingBackMove command) {
        if (moveHistory.peekLastMove() == null) {
            throw new NoMovesPerformedException(gameId);
        }
        gameInfo.proposeTakingBackMove(command);
    }

    public void rejectTakingBackLastMove(RejectTakingBackMove command) {
        if (moveHistory.peekLastMove() == null) {
            throw new NoMovesPerformedException(gameId);
        }
        gameInfo.rejectTakingBackLastMoveOffer(command);
    }

    public void acceptTakingBackLastMove(AcceptTakingBackMove command) {
        gameInfo.acceptTakingBackLastMoveOffer(command);
        Move moveTakenBack = moveHistory.takeBackLastMove();
        board.applyPositionByFenString(moveTakenBack.fenBoardStringBeforeMove());
    }

    public void resetCachedMovesOfPieces() {
        board.piecesLocations().forEach(Piece::resetCachedReachableSquares);
    }

    private void throwIfNoBlackPlayer() {
        if (gameInfo.blackPlayer() == null) {
            throw new NoBlackPlayerException();
        }
    }

    public FenBoardString establishFenBoardString() {
        String position = board().establishFenPosition();
        String currentPlayerColor = gameInfo.currentPlayerColor().color() == WHITE ? "w" : "b";
        String castlingAvailability = moveHistory.castlingAvailabilityFen();
        String enPassantPossibleSquare = moveHistory.isEnPassantPossibleFen();
        String halfMoveClock = moveHistory.halfMoveClockFen();
        String fullMoveCount = moveHistory.fullMoveCountFen();

        return new FenBoardString(
            position,
            currentPlayerColor,
            castlingAvailability,
            enPassantPossibleSquare,
            halfMoveClock,
            fullMoveCount
        );
    }

    public static Game generateNewBoard(JoinOrInitializeNewGame joinOrInitializeNewGame) {
        return new Game(
            new GameId(UUID.randomUUID()),
            joinOrInitializeNewGame.fen(),
            new MoveHistory(),
            joinOrInitializeNewGame.username()
        );
    }

}
