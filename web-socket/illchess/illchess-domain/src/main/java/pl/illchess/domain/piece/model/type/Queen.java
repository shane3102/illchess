package pl.illchess.domain.piece.model.type;

import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.PieceCapableOfPinning;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Queen extends PieceCapableOfPinning {
    private final PieceColor color;
    private Square square;

    public Queen(
            PieceColor color,
            Square square
    ) {
        this.color = color;
        this.square = square;
    }

    @Override
    public Set<Square> standardLegalMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        return extractStandardQueenMoves(piecesLocations);
    }

    @Override
    public Set<Square> attackingRayOfSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations, Move lastPerformedMove) {
        return getQueenAttackingRayOfSquare(possibleAttackedSquare, piecesLocations);
    }

    @Override
    public Set<Square> pinningRayIfImPinning(Piece askingPiece, King enemyKing, PiecesLocations piecesLocations) {
        return getQueenPinningRay(askingPiece, enemyKing, piecesLocations);
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
        return new PieceType("QUEEN");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Queen) obj;
        return Objects.equals(this.color, that.color) &&
                Objects.equals(this.square, that.square);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, square);
    }

    @Override
    public String toString() {
        return "Queen[" +
                "color=" + color + ", " +
                "square=" + square + ']';
    }

    private Set<Square> extractStandardQueenMoves(PiecesLocations piecesLocations) {
        return Stream.of(
                        square.getFile().getContainedSquares().getConnectedUntilPieceEncountered(square, color, piecesLocations),
                        square.getRank().getContainedSquares().getConnectedUntilPieceEncountered(square, color, piecesLocations),
                        square.getSquareDiagonal1().getContainedSquares().getConnectedUntilPieceEncountered(square, color, piecesLocations),
                        square.getSquareDiagonal2().getContainedSquares().getConnectedUntilPieceEncountered(square, color, piecesLocations)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Square> getQueenAttackingRayOfSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations) {

        return Stream.of(
                        square.getFile().getContainedSquares().getAttackRayOnGivenSquare(square, possibleAttackedSquare, color, piecesLocations),
                        square.getRank().getContainedSquares().getAttackRayOnGivenSquare(square, possibleAttackedSquare, color, piecesLocations),
                        square.getSquareDiagonal1().getContainedSquares().getAttackRayOnGivenSquare(square, possibleAttackedSquare, color, piecesLocations),
                        square.getSquareDiagonal2().getContainedSquares().getAttackRayOnGivenSquare(square, possibleAttackedSquare, color, piecesLocations)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Square> getQueenPinningRay(
            Piece askingPiece,
            King enemyKing,
            PiecesLocations piecesLocations
    ) {
        return Stream.of(
                        askingPiece.square().getRank().getContainedSquares().getPinningRayBySquare(
                                        askingPiece.square(),
                                        this.square,
                                        enemyKing.square(),
                                        askingPiece.color(),
                                        piecesLocations
                                )
                                .stream(),
                        askingPiece.square().getFile().getContainedSquares().getPinningRayBySquare(
                                        askingPiece.square(),
                                        this.square,
                                        enemyKing.square(),
                                        askingPiece.color(),
                                        piecesLocations
                                )
                                .stream(),
                        askingPiece.square().getSquareDiagonal1().getContainedSquares().getPinningRayBySquare(
                                        askingPiece.square(),
                                        this.square,
                                        enemyKing.square(),
                                        askingPiece.color(),
                                        piecesLocations
                                )
                                .stream(),
                        askingPiece.square().getSquareDiagonal2().getContainedSquares().getPinningRayBySquare(
                                        askingPiece.square(),
                                        this.square,
                                        enemyKing.square(),
                                        askingPiece.color(),
                                        piecesLocations
                                )
                                .stream()
                )
                .flatMap(it -> it)
                .collect(Collectors.toSet());
    }

}
