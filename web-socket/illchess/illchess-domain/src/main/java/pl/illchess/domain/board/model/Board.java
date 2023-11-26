package pl.illchess.domain.board.model;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceColorIncorrectException;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.piece.info.PieceColor;

import java.util.Objects;

public record Board(
        BoardId boardId,
        PiecesLocations piecesLocations,
        PieceColor currentPlayerColor,
        MoveHistory moveHistory
) {

    public void movePiece(MovePiece command) {
        PieceColor movedPieceColor = command.movedPiece().color();
        if (!Objects.equals(movedPieceColor, currentPlayerColor())) {
            throw new PieceColorIncorrectException(movedPieceColor, currentPlayerColor());
        }
        Move performedMove = piecesLocations().movePiece(command);
        moveHistory().addMoveToHistory(performedMove);
    }

    public void takeBackMove() {
        Move moveTakenBack = moveHistory().takeBackLastMove();
        piecesLocations.takeBackMove(moveTakenBack);
    }

    public static Board generateNewBoard(BoardId boardId) {
        return new Board(
                boardId,
                PiecesLocations.createBasicBoard(),
                PieceColor.WHITE,
                new MoveHistory()
        );
    }

}
