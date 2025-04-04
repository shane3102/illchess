package pl.illchess.game.domain.board.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;
import pl.illchess.game.domain.board.command.AcceptDraw;
import pl.illchess.game.domain.board.command.AcceptTakingBackMove;
import pl.illchess.game.domain.board.command.CheckLegalMoves;
import pl.illchess.game.domain.board.command.JoinOrInitializeNewGame;
import pl.illchess.game.domain.board.command.MovePiece;
import pl.illchess.game.domain.board.command.ProposeDraw;
import pl.illchess.game.domain.board.command.ProposeTakingBackMove;
import pl.illchess.game.domain.board.command.RejectDraw;
import pl.illchess.game.domain.board.command.RejectTakingBackMove;
import pl.illchess.game.domain.board.command.Resign;
import pl.illchess.game.domain.board.exception.InvalidUserPerformedMoveException;
import pl.illchess.game.domain.board.exception.NoBlackPlayerException;
import pl.illchess.game.domain.board.exception.NoMovesPerformedException;
import pl.illchess.game.domain.board.exception.PieceCantMoveToGivenSquareException;
import pl.illchess.game.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.game.domain.board.exception.PieceIsAlreadyOnSquareUserTriesToPlaceItOnException;
import pl.illchess.game.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.game.domain.board.model.history.Move;
import pl.illchess.game.domain.board.model.history.MoveHistory;
import pl.illchess.game.domain.board.model.square.PiecesLocations;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.board.model.state.BoardState;
import pl.illchess.game.domain.board.model.state.GameResultCause;
import pl.illchess.game.domain.board.model.state.GameState;
import pl.illchess.game.domain.board.model.state.player.Player;
import pl.illchess.game.domain.board.model.state.player.PreMove;
import pl.illchess.game.domain.board.model.state.player.Username;
import pl.illchess.game.domain.commons.model.MoveType;
import pl.illchess.game.domain.piece.exception.KingNotFoundOnBoardException;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.type.Bishop;
import pl.illchess.game.domain.piece.model.type.King;
import pl.illchess.game.domain.piece.model.type.Knight;
import static pl.illchess.game.domain.piece.model.info.PieceColor.BLACK;
import static pl.illchess.game.domain.piece.model.info.PieceColor.WHITE;

public record Board(
    BoardId boardId,
    PiecesLocations piecesLocations,
    MoveHistory moveHistory,
    BoardState boardState
) {

    public Board(
        BoardId boardId,
        FenBoardString fenString,
        MoveHistory moveHistory,
        Username username
    ) {
        this(
            boardId,
            PiecesLocations.fromFENString(fenString),
            moveHistory,
            BoardState.fromFenStringAndByUsername(fenString, username)
        );
    }

    public MoveType movePieceOrAddPreMove(MovePiece command) {
        throwIfNoBlackPlayer();
        if (boardState.isPreMove(command)) {
            addPreMove(command);
            return MoveType.PRE_MOVE;
        } else {
            movePiece(command);
            return MoveType.MOVE;
        }
    }

    private void movePiece(MovePiece command) {
        Piece movedPiece = piecesLocations().findPieceOnSquare(command.startSquare())
            .orElseThrow(() -> new PieceNotPresentOnGivenSquare(command.boardId(), command.startSquare()));

        if (Objects.equals(movedPiece.square(), command.targetSquare())) {
            throw new PieceIsAlreadyOnSquareUserTriesToPlaceItOnException(movedPiece, command.targetSquare());
        }

        boardState().checkIfAllowedToMove(boardId, movedPiece, command.username());

        Set<Square> possibleMoves = movedPiece.possibleMoves(piecesLocations, moveHistory);

        if (!possibleMoves.contains(command.targetSquare())) {
            throw new PieceCantMoveToGivenSquareException(
                movedPiece,
                command.targetSquare(),
                possibleMoves,
                command.boardId()
            );
        }

        FenBoardString fenBoardString = establishFenBoardString();

        Move performedMove = piecesLocations().movePiece(command, movedPiece, moveHistory().peekLastMove(), fenBoardString);
        resetCachedMovesOfPieces();
        moveHistory().addMoveToHistory(performedMove);
        boardState().invertCurrentPlayerColor();

        refreshPreMovesOfPlayers();
    }

    private void refreshPreMovesOfPlayers() {
        Player currentPlayer = boardState().currentPlayer();
        if (currentPlayer != null && !currentPlayer.preMoves().isEmpty()) {
            PreMove preMove = currentPlayer.preMoves().removeFirst();
            MovePiece scheduledPreMoveCommand = preMove.toCommand(boardId, boardState.currentPlayer().username());
            try {
                movePieceOrAddPreMove(scheduledPreMoveCommand);
            } catch (PieceCantMoveToGivenSquareException | PieceColorIncorrectException ignored) {
                currentPlayer.preMoves().clear();
            }
            return;
        }
        Player inactivePlayer = boardState.inactivePlayer();
        if (inactivePlayer != null && !inactivePlayer.preMoves().isEmpty()) {
            List<MovePiece> movePiecePreMoveCommands = inactivePlayer.preMoves().stream()
                .map(preMove -> preMove.toCommand(boardId, inactivePlayer.username()))
                .toList();
            inactivePlayer.preMoves().clear();
            movePiecePreMoveCommands.forEach(this::movePieceOrAddPreMove);
        }
    }

    private void addPreMove(MovePiece command) {
        Optional<PiecesLocations> lastLocationsOnPreviousPreMove = boardState().getLastLocationsOnPreviousPreMove(command.username());
        PiecesLocations clonedBoardBeforePreMove = lastLocationsOnPreviousPreMove.orElse(piecesLocations()).cloneBoard();

        Piece movedPiece = clonedBoardBeforePreMove.findPieceOnSquare(command.startSquare())
            .orElseThrow(() -> new PieceNotPresentOnGivenSquare(command.boardId(), command.startSquare()));

        Player ownerOfPiece = movedPiece.color() == WHITE ? boardState.whitePlayer() : boardState.blackPlayer();
        if (!Objects.equals(ownerOfPiece.username(), command.username())) {
            throw new InvalidUserPerformedMoveException(boardId, command.startSquare(), command.username(), ownerOfPiece.username());
        }

        Set<Square> possibleMovesOnPreMove = movedPiece.possibleMovesOnPreMove(clonedBoardBeforePreMove, moveHistory());

        if (!possibleMovesOnPreMove.contains(command.targetSquare())) {
            throw new PieceCantMoveToGivenSquareException(
                movedPiece,
                command.targetSquare(),
                possibleMovesOnPreMove,
                command.boardId()
            );
        }
        PreMove preMove = clonedBoardBeforePreMove.movePieceOnPreMove(command, movedPiece);
        Player preMovingPlayer = boardState.getPlayerByUsername(command.username())
            .orElseThrow(
                () -> new InvalidUserPerformedMoveException(command.boardId(), command.startSquare(), command.username(), boardState.currentPlayer().username())
            );
        preMovingPlayer.preMoves().addLast(preMove);

    }

    public Set<Square> legalMoves(CheckLegalMoves command) {
        throwIfNoBlackPlayer();
        Optional<Player> playerByUsername = boardState.getPlayerByUsername(command.username());

        if (playerByUsername.isEmpty() || playerByUsername.get().equals(boardState.currentPlayer())) {
            Piece movedPiece = piecesLocations.findPieceOnSquare(command.square())
                .orElseThrow(() -> new PieceNotPresentOnGivenSquare(boardId, command.square()));
            if (!Objects.equals(movedPiece.color(), boardState().currentPlayerColor().color())) {
                throw new PieceColorIncorrectException(
                    boardId,
                    movedPiece.color(),
                    boardState().currentPlayerColor().color(),
                    movedPiece.square()
                );
            }
            return movedPiece.possibleMoves(piecesLocations, moveHistory);
        }
        if (!playerByUsername.get().preMoves().isEmpty()) {
            Piece movedPiece = playerByUsername.get().preMoves().getLast().piecesLocationsAfterPreMove().findPieceOnSquare(command.square())
                .orElseThrow(() -> new PieceNotPresentOnGivenSquare(boardId, command.square()));
            return movedPiece.possibleMovesOnPreMove(piecesLocations, moveHistory);
        }
        Piece movedPiece = piecesLocations.findPieceOnSquare(command.square())
            .orElseThrow(() -> new PieceNotPresentOnGivenSquare(boardId, command.square()));

        if (movedPiece.color() != boardState.currentPlayerColor().color() && !Objects.equals(boardState.currentPlayer(), playerByUsername.get())) {
            return movedPiece.possibleMovesOnPreMove(piecesLocations, moveHistory);
        }

        return Set.of();
    }

    public GameState establishBoardState() {
        King king = (King) piecesLocations.getPieceByTypeAndColor(King.class, boardState().currentPlayerColor().color())
            .orElseThrow(KingNotFoundOnBoardException::new);

        boolean kingIsAttacked = piecesLocations.getEnemyPieces(boardState().currentPlayerColor().color())
            .stream()
            .anyMatch(piece -> piece.isAttackingSquare(king.square(), piecesLocations, moveHistory.peekLastMove()));

        boolean anyPieceCanMove = piecesLocations.getAlliedPieces(boardState().currentPlayerColor().color()).stream()
            .anyMatch(piece -> !piece.possibleMoves(piecesLocations, moveHistory).isEmpty());

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
            boardState.changeState(establishedState, establishedGameResultCause);
        }

        return establishedState;
    }

    public boolean isInsufficientMaterial() {
        Supplier<Stream<Piece>> piecesLocationsSupplier = () -> piecesLocations.locations().stream();

        List<Piece> whitePieces = piecesLocationsSupplier.get().filter(piece -> piece.color() == WHITE).toList();
        List<Piece> blackPieces = piecesLocationsSupplier.get().filter(piece -> piece.color() == BLACK).toList();

        boolean isWhiteInsufficientMaterial = whitePieces.size() == 1 || (whitePieces.size() == 2 && whitePieces.stream().anyMatch(piece -> piece instanceof Bishop || piece instanceof Knight));
        boolean isBlackInsufficientMaterial = blackPieces.size() == 1 || (blackPieces.size() == 2 && blackPieces.stream().anyMatch(piece -> piece instanceof Bishop || piece instanceof Knight));
        boolean insufficientMaterialSpecialCase = blackPieces.size() == 1 && (whitePieces.size() == 3 && whitePieces.stream().filter(piece -> piece instanceof Knight).count() == 2) ||
            whitePieces.size() == 1 && (blackPieces.size() == 3 && blackPieces.stream().filter(piece -> piece instanceof Knight).count() == 2);

        return isWhiteInsufficientMaterial && isBlackInsufficientMaterial || insufficientMaterialSpecialCase;
    }

    public void assignSecondPlayer(Username username) {
        boardState.setBlackPlayer(new Player(username));
    }

    public void resign(Resign command) {
        boardState.resign(command);
    }

    public void proposeDraw(ProposeDraw command) {
        boardState.proposeDraw(command);
    }

    public void acceptDraw(AcceptDraw command) {
        boardState.acceptDrawOffer(command);
    }

    public void rejectDraw(RejectDraw command) {
        boardState.rejectDrawOffer(command);
    }

    public void proposeTakingBackMove(ProposeTakingBackMove command) {
        if (moveHistory.peekLastMove() == null) {
            throw new NoMovesPerformedException(boardId);
        }
        boardState.proposeTakingBackMove(command);
    }

    public void rejectTakingBackLastMove(RejectTakingBackMove command) {
        if (moveHistory.peekLastMove() == null) {
            throw new NoMovesPerformedException(boardId);
        }
        boardState.rejectTakingBackLastMoveOffer(command);
    }

    public void acceptTakingBackLastMove(AcceptTakingBackMove command) {
        boardState.acceptTakingBackLastMoveOffer(command);
        Move moveTakenBack = moveHistory.takeBackLastMove();
        piecesLocations.applyPositionByFenString(moveTakenBack.fenBoardStringBeforeMove());
    }

    public void resetCachedMovesOfPieces() {
        piecesLocations.locations().forEach(Piece::resetCachedReachableSquares);
    }

    private void throwIfNoBlackPlayer() {
        if (boardState.blackPlayer() == null) {
            throw new NoBlackPlayerException();
        }
    }

    public FenBoardString establishFenBoardString() {
        String position = piecesLocations().establishFenPosition();
        String currentPlayerColor = boardState.currentPlayerColor().color() == WHITE ? "w" : "b";
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

    public static Board generateNewBoard(JoinOrInitializeNewGame joinOrInitializeNewGame) {
        return new Board(
            new BoardId(UUID.randomUUID()),
            joinOrInitializeNewGame.fen(),
            new MoveHistory(),
            joinOrInitializeNewGame.username()
        );
    }

}
