package pl.illchess.game.domain.piece.model.type;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pl.illchess.game.domain.game.model.history.Move;
import pl.illchess.game.domain.game.model.history.MoveHistory;
import pl.illchess.game.domain.game.model.square.Board;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.exception.PromotedPieceTargetTypeNotSupported;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.info.PieceAttackingRay;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.info.PieceType;

public final class Pawn implements Piece {

    private static final Set<Class<? extends Piece>> SUPPORTED_PROMOTION_TYPES = Set.of(Queen.class, Knight.class, Rook.class, Bishop.class);

    private final PieceColor color;
    private Square square;
    private Set<Square> cachedReachableSquares;

    public Pawn(
        PieceColor color,
        Square square
    ) {
        this.color = color;
        this.square = square;
        this.cachedReachableSquares = Set.of();
    }

    public Pawn(PieceColor color, Square square, Set<Square> cachedReachableSquares) {
        this.color = color;
        this.square = square;
        this.cachedReachableSquares = cachedReachableSquares;
    }

    @Override
    public Set<Square> possibleMovesOnPreMove(Board board, MoveHistory moveHistory) {
        return Stream.concat(
                standardLegalMoves(board, moveHistory.peekLastMove()).stream(),
                getStandardPawnCaptureSquares().stream().filter(this::filterByPawnColor)
            )
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Square> standardLegalMoves(Board board, Move lastPerformedMove) {
        return extractPawnMoves(board, lastPerformedMove);
    }

    @Override
    public PieceAttackingRay attackingRayOfSquare(Square possibleAttackedSquare, Board board, Move lastPerformedMove) {
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
    public Set<Square> cachedReachableSquares() {
        return cachedReachableSquares;
    }

    @Override
    public void setCachedReachableSquares(Set<Square> cachedReachableSquares) {
        this.cachedReachableSquares = cachedReachableSquares;
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

    public Piece promotePawn(PieceType pawnPromotedToPieceType) {
        Piece pieceByPieceType = PieceType.getPieceByPieceType(pawnPromotedToPieceType, color, square, Set.of());
        if (SUPPORTED_PROMOTION_TYPES.stream().noneMatch(supportedType -> supportedType.equals(pieceByPieceType.getClass()))) {
            throw new PromotedPieceTargetTypeNotSupported(pieceByPieceType.getClass(), SUPPORTED_PROMOTION_TYPES);
        }
        return pieceByPieceType;
    }

    private Set<Square> extractPawnMoves(Board board, Move lastPerformedMove) {
        Set<Square> standardPawnMovement = getStandardPawnMovement(board);

        Set<Square> standardCaptures = getStandardPawnPossibleCaptures(board);

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

    private Set<Square> getStandardPawnMovement(Board board) {
        Set<Square> result = square.getFile().getContainedSquares().getClosestNonOccupiedNeighbours(square, board);
        if (isOnStartingSquare()) {
            result = Stream.concat(
                    Stream.of(result),
                    result.stream().map(
                        it -> it.getFile()
                            .getContainedSquares()
                            .getClosestNonOccupiedNeighbours(it, board)
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

    private Set<Square> getStandardPawnPossibleCaptures(Board board) {
        return Stream.concat(
                square.getSquareDiagonal1().getContainedSquares().getClosestOccupiedNeighbours(square, board).stream(),
                square.getSquareDiagonal2().getContainedSquares().getClosestOccupiedNeighbours(square, board).stream()
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

    private Set<Square> getStandardPawnCaptureSquares() {
        return Stream.concat(
                square.getSquareDiagonal1().getContainedSquares().getClosestNeighbours(square).stream(),
                square.getSquareDiagonal2().getContainedSquares().getClosestNeighbours(square).stream()
            )
            .collect(Collectors.toSet());
    }

    private boolean isEnPassantPossible(
        Move lastPerformedMove
    ) {
        boolean whiteEnPassant = (
            color.equals(PieceColor.WHITE)
                && lastPerformedMove.movedPiece() instanceof Pawn
                && lastPerformedMove.targetSquare().getRank().getNumber() == 5
                && square.getRank().getNumber() == 5
                &&
                (
                    square.getFile().getNumber() - 1 == lastPerformedMove.targetSquare().getFile().getNumber()
                        || square.getFile().getNumber() + 1 == lastPerformedMove.targetSquare().getFile().getNumber()
                )
        );

        boolean blackEnPassant = (
            color.equals(PieceColor.BLACK)
                && lastPerformedMove.movedPiece() instanceof Pawn
                && lastPerformedMove.targetSquare().getRank().getNumber() == 4
                && square.getRank().getNumber() == 4
                &&
                (
                    square.getFile().getNumber() - 1 == lastPerformedMove.targetSquare().getFile().getNumber()
                        || square.getFile().getNumber() + 1 == lastPerformedMove.targetSquare().getFile().getNumber()
                )
        );

        return whiteEnPassant || blackEnPassant;
    }

    private boolean filterByPawnColor(Square comparedSquare) {
        return color == PieceColor.WHITE
            ? comparedSquare.isHigher(square)
            : comparedSquare.isLower(square);
    }

    @Override
    public Piece clonePiece() {
        return new Pawn(color(), square());
    }
}
