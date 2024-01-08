package pl.illchess.domain.piece.model.type;

import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.PieceBehaviour;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class King extends PieceBehaviour {
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
    public Set<Square> possibleMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        Set<Square> standardKingMovement = extractStandardKingMovement();

        return standardKingMovement.stream()
                .filter(square -> checkIfKingCanMove(square, piecesLocations, lastPerformedMove))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isDefendingSquare(Square square, PiecesLocations piecesLocations, Move lastPerformedMove) {
        return extractStandardKingMovement().stream()
                .anyMatch(checkedSquare -> Objects.equals(checkedSquare.name(), square.name()));
    }

    private Set<Square> extractStandardKingMovement() {
        return Stream.of(
                        square.getFile().getContainedSquares().getClosestNeighbours(square),
                        square.getRank().getContainedSquares().getClosestNeighbours(square),
                        square.getSquareDiagonal1().getContainedSquares().getClosestNeighbours(square),
                        square.getSquareDiagonal2().getContainedSquares().getClosestNeighbours(square)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private boolean checkIfKingCanMove(
            Square square,
            PiecesLocations piecesLocations,
            Move lastPerformedMove
    ) {
        Optional<PieceBehaviour> pieceOnSquare = piecesLocations.getPieceOnSquare(square);

        return pieceOnSquare.map(this::isSamePieceOnSquare).orElse(false) || isEnemyDefendingSquare(square, piecesLocations, lastPerformedMove);
    }

    private boolean isEnemyDefendingSquare(
            Square square,
            PiecesLocations piecesLocations,
            Move lastPerformedMove
    ) {
        Set<PieceBehaviour> enemyPieces = piecesLocations.getEnemyPieces(color);

        return enemyPieces
                .stream()
                .noneMatch(
                        piece -> piece.isDefendingSquare(square, piecesLocations, lastPerformedMove)
                );
    }

    private boolean isSamePieceOnSquare(PieceBehaviour pieceOnSquare) {
        return Objects.equals(pieceOnSquare.color(), this.color);
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
