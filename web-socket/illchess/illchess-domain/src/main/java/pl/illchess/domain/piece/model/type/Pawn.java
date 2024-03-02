package pl.illchess.domain.piece.model.type;

import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.PieceAttackingRay;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Pawn implements Piece {
    private final PieceColor color;
    private Square square;

    public Pawn(
            PieceColor color,
            Square square
    ) {
        this.color = color;
        this.square = square;
    }

    @Override
    public Set<Square> standardLegalMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        return extractPawnMoves(piecesLocations, lastPerformedMove);
    }

    @Override
    public PieceAttackingRay attackingRayOfSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations, Move lastPerformedMove) {
        Set<Square> pawnAttackingRay = Stream.concat(
                this.square.getSquareDiagonal1().getContainedSquares().getClosestNeighbours(this.square).stream().filter(capturableSquare -> Objects.equals(capturableSquare.name(), possibleAttackedSquare.name())),
                this.square.getSquareDiagonal2().getContainedSquares().getClosestNeighbours(this.square).stream().filter(capturableSquare -> Objects.equals(capturableSquare.name(), possibleAttackedSquare.name()))
            )
            .filter(this::filterByPawnColor)
            .collect(Collectors.toSet());
        return new PieceAttackingRay(square, pawnAttackingRay);
    }

    public PieceColor color() {
        return color;
    }

    public Square square() {
        return square;
    }

    @Override
    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public PieceType typeName() {
        return new PieceType("PAWN");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Pawn) obj;
        return Objects.equals(this.color, that.color) &&
                Objects.equals(this.square, that.square);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, square);
    }

    @Override
    public String toString() {
        return "Pawn[" +
                "color=" + color + ", " +
                "square=" + square + ']';
    }

    public Square getSquareOfPieceCapturedEnPassant(Square targetSquareOfPerformedMove, Move lastPerformedMove) {
        if (getEnPassantPossibleCaptures(lastPerformedMove).contains(targetSquareOfPerformedMove)) {
            return lastPerformedMove.targetSquare();
        }
        return null;
    }

    private Set<Square> extractPawnMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        Set<Square> standardPawnMovement = getStandardPawnMovement(piecesLocations);

        Set<Square> standardCaptures = getStandardPawnPossibleCaptures(piecesLocations);

        Set<Square> enPassantPossibleCaptures = getEnPassantPossibleCaptures(lastPerformedMove);

        return Stream.of(
                        standardPawnMovement.stream(),
                        standardCaptures.stream(),
                        enPassantPossibleCaptures.stream()
                )
                .flatMap(it -> it)
                .filter(this::filterByPawnColor)
                .collect(Collectors.toSet());
    }

    private Set<Square> getStandardPawnMovement(PiecesLocations piecesLocations) {
        Set<Square> result = square.getFile().getContainedSquares().getClosestNonOccupiedNeighbours(square, piecesLocations);
        if (isOnStartingSquare()) {
            result = Stream.concat(
                            Stream.of(result),
                            result.stream().map(
                                    it -> it.getFile()
                                            .getContainedSquares()
                                            .getClosestNonOccupiedNeighbours(it, piecesLocations)
                            )
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }
        return result;
    }

    private boolean isOnStartingSquare() {
        return (this.square.getRank().getNumber() == 2 && Objects.equals(this.color, PieceColor.WHITE)) ||
                (this.square.getRank().getNumber() == 7 && Objects.equals(this.color, PieceColor.BLACK));
    }

    private Set<Square> getStandardPawnPossibleCaptures(PiecesLocations piecesLocations) {
        return Stream.concat(
                        square.getSquareDiagonal1().getContainedSquares().getClosestOccupiedNeighbours(square, piecesLocations).stream(),
                        square.getSquareDiagonal2().getContainedSquares().getClosestOccupiedNeighbours(square, piecesLocations).stream()
                )
                .collect(Collectors.toSet());
    }

    private Set<Square> getEnPassantPossibleCaptures(
            Move lastPerformedMove
    ) {
        if (lastPerformedMove == null) {
            return Set.of();
        }
        if (isEnPassantPossible(lastPerformedMove)) {
            return lastPerformedMove.targetSquare()
                    .getFile()
                    .getContainedSquares()
                    .getClosestNeighbours(lastPerformedMove.targetSquare());
        }
        return Set.of();
    }

    private boolean isEnPassantPossible(
            Move lastPerformedMove
    ) {
        boolean whiteEnPassant = (
                color.equals(PieceColor.WHITE)
                        && lastPerformedMove.movedPiece() instanceof Pawn
                        && lastPerformedMove.targetSquare().getRank().getNumber() == 5
                        && square.getRank().getNumber() == 5
        );

        boolean blackEnPassant = (
                color.equals(PieceColor.BLACK)
                        && lastPerformedMove.movedPiece() instanceof Pawn
                        && lastPerformedMove.targetSquare().getRank().getNumber() == 4
                        && square.getRank().getNumber() == 4
        );

        return whiteEnPassant || blackEnPassant;
    }

    private boolean filterByPawnColor(Square comparedSquare) {
        return color == PieceColor.WHITE
                ? comparedSquare.isHigher(square)
                : comparedSquare.isLower(square);
    }

}
