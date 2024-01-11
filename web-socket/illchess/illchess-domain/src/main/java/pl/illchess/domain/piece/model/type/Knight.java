package pl.illchess.domain.piece.model.type;

import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Knight extends Piece {
    private final PieceColor color;
    private Square square;

    public Knight(
            PieceColor color,
            Square square
    ) {
        this.color = color;
        this.square = square;
    }

    @Override
    public Set<Square> standardLegalMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        return extractStandardKnightMovement();
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


}
