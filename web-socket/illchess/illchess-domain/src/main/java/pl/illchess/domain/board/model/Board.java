package pl.illchess.domain.board.model;

import pl.illchess.domain.board.command.InitializeNewBoard;
import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceCantMoveToGivenSquareException;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.state.BoardState;
import pl.illchess.domain.board.model.state.GameState;
import pl.illchess.domain.piece.exception.KingNotFoundOnBoardException;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.type.King;

import java.util.Objects;
import java.util.Set;

public record Board(
        BoardId boardId,
        PiecesLocations piecesLocations,
        MoveHistory moveHistory,
        BoardState boardState
) {

    public void movePiece(MovePiece command) {
        Piece movedPiece = command.movedPiece();
        PieceColor movedPieceColor = movedPiece.color();
        if (!Objects.equals(movedPieceColor, boardState().currentPlayerColor().color())) {
            throw new PieceColorIncorrectException(
                    boardId,
                    movedPieceColor,
                    boardState().currentPlayerColor().color(),
                    movedPiece.square()
            );
        }
        // TODO cache it (somehow)
        Set<Square> possibleMoves = movedPiece.possibleMoves(piecesLocations, moveHistory.peekLastMove());

        if (!possibleMoves.contains(command.targetSquare())) {
            throw new PieceCantMoveToGivenSquareException(
                    movedPiece,
                    command.targetSquare(),
                    possibleMoves,
                    command.boardId()
            );
        }

        Move performedMove = piecesLocations().movePiece(command, moveHistory().peekLastMove());
        moveHistory().addMoveToHistory(performedMove);
        boardState().invertCurrentPlayerColor();
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
                .anyMatch(piece -> !piece.possibleMoves(piecesLocations, moveHistory.peekLastMove()).isEmpty());

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

    public static Board generateNewBoard(InitializeNewBoard initializeNewBoard) {
        return new Board(
                initializeNewBoard.boardId(),
                PiecesLocations.createBasicBoard(),
                new MoveHistory(),
                BoardState.defaultState()
        );
    }

}
