package pl.illchess.game.domain.piece.model.type;

import pl.illchess.game.domain.game.model.history.Move;
import pl.illchess.game.domain.game.model.history.MoveHistory;
import pl.illchess.game.domain.game.model.square.Board;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.PieceCapableOfPinning;
import pl.illchess.game.domain.piece.model.info.PieceAttackingRay;
import pl.illchess.game.domain.piece.model.info.PieceAttackingRay.SquaresInRay;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.info.PieceType;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Bishop implements PieceCapableOfPinning {
    private final PieceColor color;
    private Square square;
    private Set<Square> cachedReachableSquares;

    public Bishop(
        PieceColor color,
        Square square
    ) {
        this.color = color;
        this.square = square;
        this.cachedReachableSquares = Set.of();
    }

    public Bishop(PieceColor color, Square square, Set<Square> cachedReachableSquares) {
        this.color = color;
        this.square = square;
        this.cachedReachableSquares = cachedReachableSquares;
    }

    @Override
    public Set<Square> possibleMovesOnPreMove(Board board, MoveHistory moveHistory) {
        return Stream.of(
                square.getSquareDiagonal1().getContainedSquares().getFullRay(),
                square.getSquareDiagonal2().getContainedSquares().getFullRay()
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Square> standardLegalMoves(Board board, Move lastPerformedMove) {
        return Stream.of(
                square.getSquareDiagonal1().getContainedSquares().getConnectedUntilPieceEncountered(square, color, board),
                square.getSquareDiagonal2().getContainedSquares().getConnectedUntilPieceEncountered(square, color, board)
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    @Override
    public PieceAttackingRay attackingRayOfSquare(Square possibleAttackedSquare, Board board, Move lastPerformedMove) {
        SquaresInRay bishopAttackingRayOfSquare = getBishopAttackingRayOfSquare(possibleAttackedSquare, board);
        return new PieceAttackingRay(square, bishopAttackingRayOfSquare);
    }

    @Override
    public Set<Square> pinningRayIfImPinning(Piece askingPiece, King enemyKing, Board board) {
        return getBishopPinningRay(askingPiece, enemyKing, board);
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

    private SquaresInRay getBishopAttackingRayOfSquare(Square possibleAttackedSquare, Board board) {
        return Stream.of(
                square.getSquareDiagonal1().getContainedSquares().getAttackRayOnGivenSquare(square, possibleAttackedSquare, color, board),
                square.getSquareDiagonal2().getContainedSquares().getAttackRayOnGivenSquare(square, possibleAttackedSquare, color, board)
            )
            .filter(it -> !it.isEmpty())
            .findFirst()
            .orElse(SquaresInRay.empty());
    }

    private Set<Square> getBishopPinningRay(
        Piece askingPiece,
        King enemyKing,
        Board board
    ) {
        return Stream.of(
                askingPiece.square().getSquareDiagonal1().getContainedSquares().getPinningRayBySquare(
                        askingPiece.square(),
                        this.square,
                        enemyKing.square(),
                        askingPiece.color(),
                        board
                    )
                    .stream(),
                askingPiece.square().getSquareDiagonal2().getContainedSquares().getPinningRayBySquare(
                        askingPiece.square(),
                        this.square,
                        enemyKing.square(),
                        askingPiece.color(),
                        board
                    )
                    .stream()
            )
            .flatMap(it -> it)
            .collect(Collectors.toSet());
    }

    @Override
    public Piece clonePiece() {
        return new Bishop(color, square);
    }
}
