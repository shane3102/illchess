package pl.illchess.domain.board.model.square;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.exception.TargetSquareOccupiedBySameColorPieceException;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.piece.Piece;
import pl.illchess.domain.piece.info.PieceColor;
import pl.illchess.domain.piece.info.PieceType;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static pl.illchess.domain.piece.info.PieceColor.BLACK;
import static pl.illchess.domain.piece.info.PieceColor.WHITE;
import static pl.illchess.domain.piece.info.PieceType.BISHOP;
import static pl.illchess.domain.piece.info.PieceType.KING;
import static pl.illchess.domain.piece.info.PieceType.KNIGHT;
import static pl.illchess.domain.piece.info.PieceType.PAWN;
import static pl.illchess.domain.piece.info.PieceType.QUEEN;
import static pl.illchess.domain.piece.info.PieceType.ROOK;

public record PiecesLocations(
        Map<Square, Piece> locations
) {

    public Move movePiece(MovePiece command) {
        if (!isPieceOnLocation(command)) {
            throw new PieceNotPresentOnGivenSquare(
                    command.boardId(),
                    command.movedPiece(),
                    command.currentSquare()
            );
        }

        if (isSquareOccupiedBySameColorPiece(command)) {
            throw new TargetSquareOccupiedBySameColorPieceException(
                    command.boardId(),
                    command.currentSquare(),
                    command.targetSquare()
            );
        }

        Piece capturedPiece = locations.get(command.targetSquare());

        locations.put(command.targetSquare(), command.movedPiece());
        locations.remove(command.currentSquare());

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

    public static PiecesLocations createBasicBoard() {
        return new PiecesLocations(
                new HashMap<>(
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(Square.A1, new Piece(WHITE, ROOK)),
                                new AbstractMap.SimpleEntry<>(Square.B1, new Piece(WHITE, KNIGHT)),
                                new AbstractMap.SimpleEntry<>(Square.C1, new Piece(WHITE, BISHOP)),
                                new AbstractMap.SimpleEntry<>(Square.D1, new Piece(WHITE, QUEEN)),
                                new AbstractMap.SimpleEntry<>(Square.E1, new Piece(WHITE, KING)),
                                new AbstractMap.SimpleEntry<>(Square.F1, new Piece(WHITE, BISHOP)),
                                new AbstractMap.SimpleEntry<>(Square.G1, new Piece(WHITE, KNIGHT)),
                                new AbstractMap.SimpleEntry<>(Square.H1, new Piece(WHITE, ROOK)),

                                new AbstractMap.SimpleEntry<>(Square.A2, new Piece(WHITE, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.B2, new Piece(WHITE, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.C2, new Piece(WHITE, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.D2, new Piece(WHITE, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.E2, new Piece(WHITE, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.F2, new Piece(WHITE, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.G2, new Piece(WHITE, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.H2, new Piece(WHITE, PAWN)),

                                new AbstractMap.SimpleEntry<>(Square.A7, new Piece(BLACK, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.B7, new Piece(BLACK, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.C7, new Piece(BLACK, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.D7, new Piece(BLACK, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.E7, new Piece(BLACK, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.F7, new Piece(BLACK, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.G7, new Piece(BLACK, PAWN)),
                                new AbstractMap.SimpleEntry<>(Square.H7, new Piece(BLACK, PAWN)),

                                new AbstractMap.SimpleEntry<>(Square.A8, new Piece(BLACK, ROOK)),
                                new AbstractMap.SimpleEntry<>(Square.B8, new Piece(BLACK, KNIGHT)),
                                new AbstractMap.SimpleEntry<>(Square.C8, new Piece(BLACK, BISHOP)),
                                new AbstractMap.SimpleEntry<>(Square.D8, new Piece(BLACK, QUEEN)),
                                new AbstractMap.SimpleEntry<>(Square.E8, new Piece(BLACK, KING)),
                                new AbstractMap.SimpleEntry<>(Square.F8, new Piece(BLACK, BISHOP)),
                                new AbstractMap.SimpleEntry<>(Square.G8, new Piece(BLACK, KNIGHT)),
                                new AbstractMap.SimpleEntry<>(Square.H8, new Piece(BLACK, ROOK))
                        )
                )
        );
    }
}
