package pl.illchess.game.domain.piece.model.type;

import pl.illchess.game.domain.board.model.history.Move;
import pl.illchess.game.domain.board.model.square.PiecesLocations;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.info.PieceAttackingRay;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.info.PieceType;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Knight implements Piece {
    private final PieceColor color;
    private Square square;
    private Set<Square>  cachedReachableSquares;

    public Knight(
            PieceColor color,
            Square square
    ) {
        this.color = color;
        this.square = square;
        this.cachedReachableSquares = Set.of();
    }

    public Knight(PieceColor color, Square square, Set<Square> cachedReachableSquares) {
        this.color = color;
        this.square = square;
        this.cachedReachableSquares = cachedReachableSquares;
    }

    @Override
    public Set<Square> standardLegalMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        return extractStandardKnightMovement();
    }

    @Override
    public PieceAttackingRay attackingRayOfSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations, Move lastPerformedMove) {
        if (extractStandardKnightMovement().contains(possibleAttackedSquare)) {
            return new PieceAttackingRay(square, Set.of(possibleAttackedSquare));
        } else {
            return new PieceAttackingRay(square, Collections.emptySet());
        }
    }

    public PieceColor color() {
        return color;
    }

    public Square square() {
        return square;
    }

    @Override
    public Set<Square> cachedReachableSquares() {
        return cachedReachableSquares;
    }

    @Override
    public void setCachedReachableSquares(Set<Square> cachedReachableSquares) {
        this.cachedReachableSquares = cachedReachableSquares;
    }

    @Override
    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public PieceType typeName() {
        return new PieceType("KNIGHT");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Knight) obj;
        return Objects.equals(this.color, that.color) &&
                Objects.equals(this.square, that.square);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, square);
    }

    @Override
    public String toString() {
        return "Knight[" +
                "color=" + color + ", " +
                "square=" + square + ']';
    }

    private Set<Square> extractStandardKnightMovement() {
        Stream<Square> possibleSquaresStream = Stream.of(
                        square.getSquareDiagonal1()
                                .getContainedSquares()
                                .getClosestNeighbours(square)
                                .stream()
                                .map(square -> square.getFile().getContainedSquares().getClosestNeighbours(square)),
                        square.getSquareDiagonal2()
                                .getContainedSquares()
                                .getClosestNeighbours(square)
                                .stream()
                                .map(square -> square.getFile().getContainedSquares().getClosestNeighbours(square)),
                        square.getSquareDiagonal1()
                                .getContainedSquares()
                                .getClosestNeighbours(square)
                                .stream()
                                .map(square -> square.getRank().getContainedSquares().getClosestNeighbours(square)),
                        square.getSquareDiagonal2()
                                .getContainedSquares()
                                .getClosestNeighbours(square)
                                .stream()
                                .map(square -> square.getRank().getContainedSquares().getClosestNeighbours(square))
                )
                .flatMap(it -> it)
                .flatMap(Set::stream);

        Supplier<Stream<Square>> closestKnightNeighbours = () -> Stream.of(
                        square.getFile().getContainedSquares().getClosestNeighbours(square),
                        square.getRank().getContainedSquares().getClosestNeighbours(square)
                )
                .flatMap(Set::stream);

        return possibleSquaresStream.filter(
                        checkedSquare -> closestKnightNeighbours.get().noneMatch(
                                closesNeighbourSquare -> closesNeighbourSquare.equals(checkedSquare)
                        )
                )
                .collect(Collectors.toSet());
    }

    @Override
    public Piece clonePiece() {
        return new Knight(color(), square());
    }
}
