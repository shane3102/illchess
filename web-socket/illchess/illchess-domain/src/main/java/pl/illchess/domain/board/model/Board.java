package pl.illchess.domain.board.model;

import pl.illchess.domain.board.command.InitializeNewBoard;
import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceCantMoveToGivenSquareException;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.PieceBehaviour;
import pl.illchess.domain.piece.model.info.CurrentPlayerColor;
import pl.illchess.domain.piece.model.info.PieceColor;

import java.util.Objects;
import java.util.Set;

public record Board(
        BoardId boardId,
        PiecesLocations piecesLocations,
        CurrentPlayerColor currentPlayerColor,
        MoveHistory moveHistory
) {

    public void movePiece(MovePiece command) {
        PieceBehaviour movedPiece = command.movedPiece();
        PieceColor movedPieceColor = movedPiece.color();
        if (!Objects.equals(movedPieceColor, currentPlayerColor().color())) {
            throw new PieceColorIncorrectException(
                    boardId,
                    movedPieceColor,
                    currentPlayerColor().color(),
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
        currentPlayerColor.invert();
    }

    public void takeBackMove() {
        Move moveTakenBack = moveHistory().takeBackLastMove();
        piecesLocations.takeBackMove(moveTakenBack);
    }

    public static Board generateNewBoard(InitializeNewBoard initializeNewBoard) {
        return new Board(
                initializeNewBoard.boardId(),
                PiecesLocations.createBasicBoard(),
                new CurrentPlayerColor(PieceColor.WHITE),
                new MoveHistory()
        );
    }

}
