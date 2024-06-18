package pl.illchess.domain.board.model;

import pl.illchess.domain.board.command.AcceptDraw;
import pl.illchess.domain.board.command.CheckLegalMoves;
import pl.illchess.domain.board.command.JoinOrInitializeNewGame;
import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.command.ProposeDraw;
import pl.illchess.domain.board.command.RejectDraw;
import pl.illchess.domain.board.command.Resign;
import pl.illchess.domain.board.exception.PieceCantMoveToGivenSquareException;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.state.BoardState;
import pl.illchess.domain.board.model.state.GameState;
import pl.illchess.domain.board.model.state.player.IsProposingDraw;
import pl.illchess.domain.board.model.state.player.Player;
import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.piece.exception.KingNotFoundOnBoardException;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.type.King;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static pl.illchess.domain.piece.model.info.PieceColor.WHITE;

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

    public void movePiece(MovePiece command) {
        Piece movedPiece = piecesLocations().findPieceOnSquare(command.startSquare())
            .orElseThrow(() -> new PieceNotPresentOnGivenSquare(command.boardId(), command.startSquare()));
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
    }

    public Set<Square> legalMoves(CheckLegalMoves command) {
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

    public void takeBackMove() {
        Move moveTakenBack = moveHistory().takeBackLastMove();
        piecesLocations.takeBackMove(moveTakenBack);
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

        if (kingIsAttacked && !anyPieceCanMove) {
            establishedState = GameState.CHECKMATE;
        } else if (!kingIsAttacked && !anyPieceCanMove) {
            establishedState = GameState.STALEMATE;
        } else {
            establishedState = GameState.CONTINUE;
        }

        if (establishedState != GameState.CONTINUE) {
            boardState.changeState(establishedState);
        }

        return establishedState;
    }

    public void assignSecondPlayer(Username username) {
        boardState.setBlackPlayer(new Player(username, new IsProposingDraw(false)));
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

    public void resetCachedMovesOfPieces() {
        piecesLocations.locations().forEach(Piece::resetCachedReachableSquares);
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
