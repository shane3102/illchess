package pl.illchess.domain.board.model;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;

public record Board(
        BoardId boardId,
        PiecesLocations piecesLocations,
        MoveHistory moveHistory
) {

    public void movePiece(MovePiece command) {
        Move performedMove = piecesLocations().movePiece(command);
        moveHistory().addMoveToHistory(performedMove);
    }

    public void takeBackMove() {
        Move moveTakenBack = moveHistory().takeBackLastMove();
        piecesLocations.takeBackMove(moveTakenBack);
    }

}
