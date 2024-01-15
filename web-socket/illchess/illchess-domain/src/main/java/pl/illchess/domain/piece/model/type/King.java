package pl.illchess.domain.piece.model.type;

import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class King extends Piece {
    private final PieceColor color;
    private Square square;

    public King(
            PieceColor color,
            Square square
    ) {
        this.color = color;
        this.square = square;
    }

    @Override
    public Set<Square> standardLegalMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        return Stream.of(
                        square.getFile().getContainedSquares().getClosestNeighbours(square),
                        square.getRank().getContainedSquares().getClosestNeighbours(square),
                        square.getSquareDiagonal1().getContainedSquares().getClosestNeighbours(square),
                        square.getSquareDiagonal2().getContainedSquares().getClosestNeighbours(square)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Square> possibleMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        Set<Square> standardKingMovement = standardLegalMoves(piecesLocations, lastPerformedMove);

        return standardKingMovement.stream()
                .filter(square -> isEnemyAttackingSquare(square, piecesLocations, lastPerformedMove))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Square> attackingRayOfSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations, Move lastPerformedMove) {
        return standardLegalMoves(piecesLocations, lastPerformedMove).stream()
                .filter(checkedSquare -> Objects.equals(checkedSquare.name(), possibleAttackedSquare.name()))
                .collect(Collectors.toSet());
    }

    private boolean isEnemyAttackingSquare(
            Square possibleAttackedSquare,
            PiecesLocations piecesLocations,
            Move lastPerformedMove
    ) {
        Set<Piece> enemyPieces = piecesLocations.getEnemyPieces(color);

        return enemyPieces
                .stream()
                .allMatch(
                        piece -> piece.attackingRayOfSquare(possibleAttackedSquare, piecesLocations, lastPerformedMove).isEmpty()
                );
    }

    public PieceColor color() {
        return color;
    }

    public Square square() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public PieceType typeName() {
        return new PieceType("KING");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (King) obj;
        return Objects.equals(this.color, that.color) &&
                Objects.equals(this.square, that.square);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, square);
    }

    @Override
    public String toString() {
        return "King[" +
                "color=" + color + ", " +
                "square=" + square + ']';
    }

}
