package pl.illchess.domain.board.model;

import pl.illchess.domain.board.command.InitializeNewBoard;
import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.piece.info.CurrentPlayerColor;
import pl.illchess.domain.piece.info.PieceColor;

import java.util.Objects;

public record Board(
        BoardId boardId,
        PiecesLocations piecesLocations,
        CurrentPlayerColor currentPlayerColor,
        MoveHistory moveHistory
) {

    public void movePiece(MovePiece command) {
        PieceColor movedPieceColor = command.movedPiece().color();
        if (!Objects.equals(movedPieceColor, currentPlayerColor().color())) {
            throw new PieceColorIncorrectException(movedPieceColor, currentPlayerColor().color());
        }
        Move performedMove = piecesLocations().movePiece(command);
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
