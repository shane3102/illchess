package pl.illchess.domain.board.model.square;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.exception.TargetSquareOccupiedBySameColorPieceException;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.piece.Piece;
import pl.illchess.domain.piece.info.PieceColor;

import java.util.Map;
import java.util.Objects;

public record PiecesLocations(
        Map<Square, Piece> locations
) {

    public Move movePiece(MovePiece command) {
        if (!isPieceOnLocation(command)) {
            throw new PieceNotPresentOnGivenSquare();
        }

        if (isSquareOccupiedBySameColorPiece(command)) {
            throw new TargetSquareOccupiedBySameColorPieceException();
        }

        Piece capturedPiece = locations.get(command.targetSquare());

        locations.put(command.targetSquare(), command.movedPiece());

        return new Move(
                command.currentSquare(),
                command.targetSquare(),
                command.movedPiece(),
                capturedPiece
        );
    }

    public void takeBackMove(Move moveTakenBack) {
        locations.put(moveTakenBack.startSquare(), moveTakenBack.movedPiece());

        if (moveTakenBack.capturedPiece() != null) {
            locations.put(moveTakenBack.targetSquare(), moveTakenBack.movedPiece());
        }
    }

    private boolean isPieceOnLocation(MovePiece command) {
        return Objects.equals(locations.get(command.currentSquare()), command.movedPiece());
    }

    private boolean isSquareOccupiedBySameColorPiece(MovePiece command) {
        Piece possiblePiece = locations.get(command.targetSquare());

        if (possiblePiece == null) {
            return false;
        }

        PieceColor movedPieceColor = command.movedPiece().color();

        return Objects.equals(possiblePiece.color(), movedPieceColor);
    }
}
