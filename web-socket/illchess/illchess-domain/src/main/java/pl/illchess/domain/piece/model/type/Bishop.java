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

public final class Bishop extends PieceCapableOfPinning {
    private final PieceColor color;
    private Square square;

    public Bishop(
            PieceColor color,
            Square square
    ) {
        this.color = color;
        this.square = square;
    }

    @Override
    public Set<Square> standardLegalMoves(PiecesLocations piecesLocations, Move lastPerformedMove) {
        return Stream.of(
                        square.getSquareDiagonal1().getContainedSquares().getConnectedUntilPieceEncountered(square, color, piecesLocations),
                        square.getSquareDiagonal2().getContainedSquares().getConnectedUntilPieceEncountered(square, color, piecesLocations)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAttackingSquare(Square square, PiecesLocations piecesLocations, Move lastPerformedMove) {
        Set<Square> reachableSquaresXrayingKing = getBishopXrayOfEnemyKing(piecesLocations);
        return reachableSquaresXrayingKing.stream().anyMatch(checkedSquare -> Objects.equals(checkedSquare.name(), square.name()));
    }

    @Override
    public Set<Square> pinningRayIfImPinning(Piece askingPiece, King enemyKing, PiecesLocations piecesLocations) {
        return getBishopPinningRay(askingPiece, enemyKing, piecesLocations);
    }

    @Override
    public PieceType typeName() {
        return new PieceType("BISHOP");
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Bishop) obj;
        return Objects.equals(this.color, that.color) &&
                Objects.equals(this.square, that.square);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, square);
    }

    @Override
    public String toString() {
        return "Bishop[" +
                "color=" + color + ", " +
                "square=" + square + ']';
    }

    private Set<Square> getBishopXrayOfEnemyKing(PiecesLocations piecesLocations) {
        return Stream.of(
                        square.getSquareDiagonal1().getContainedSquares().getConnectedXrayingKing(square, color, piecesLocations),
                        square.getSquareDiagonal2().getContainedSquares().getConnectedXrayingKing(square, color, piecesLocations)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Square> getBishopPinningRay(
            Piece askingPiece,
            King enemyKing,
            PiecesLocations piecesLocations
    ) {
        return Stream.of(
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
