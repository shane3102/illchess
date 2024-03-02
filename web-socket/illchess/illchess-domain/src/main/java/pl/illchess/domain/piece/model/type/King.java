package pl.illchess.domain.piece.model.type;

import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.square.info.SquareFile;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.PieceAttackingRay;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.illchess.domain.piece.model.info.PieceColor.WHITE;

public final class King implements Piece {
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
    public Set<Square> possibleMoves(PiecesLocations piecesLocations, MoveHistory moveHistory) {
        Move lastPerformedMove = moveHistory.peekLastMove();

        Set<Square> standardKingMovement = standardLegalMoves(piecesLocations, lastPerformedMove);
        Set<Square> castlingSquares = getCastlingSquares(piecesLocations, moveHistory);

        Set<Square> standardMovesWithCastlingMoves = Stream.of(standardKingMovement, castlingSquares)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        Set<Square> alliedOccupiedSquares = piecesLocations.getAlliedPieces(color())
            .stream()
            .map(Piece::square)
            .collect(Collectors.toSet());

        return standardMovesWithCastlingMoves.stream()
            .filter(square -> !isEnemyAttackingSquare(square, piecesLocations, lastPerformedMove))
            .filter(square -> alliedOccupiedSquares.isEmpty() || !alliedOccupiedSquares.contains(square))
            .collect(Collectors.toSet());
    }

    @Override
    public PieceAttackingRay attackingRayOfSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations, Move lastPerformedMove) {
        Set<Square> attackingRay = standardLegalMoves(piecesLocations, lastPerformedMove).stream()
            .filter(checkedSquare -> Objects.equals(checkedSquare.name(), possibleAttackedSquare.name()))
            .collect(Collectors.toSet());
        return new PieceAttackingRay(square, attackingRay);
    }

    private boolean isEnemyAttackingSquare(
        Square possibleAttackedSquare,
        PiecesLocations piecesLocations,
        Move lastPerformedMove
    ) {
        Set<Piece> enemyPieces = piecesLocations.getEnemyPieces(color);

        return enemyPieces
            .stream()
            .anyMatch(
                piece -> piece.attackingRayOfSquare(possibleAttackedSquare, piecesLocations, lastPerformedMove)
                    .attackingRay()
                    .contains(possibleAttackedSquare)
            );
    }

    public Set<Square> getCastlingSquares(PiecesLocations piecesLocations, MoveHistory moveHistory) {
        Set<Square> result = new HashSet<>();

        Move lastPerformedMove = moveHistory.peekLastMove();

        Square castlingSquare1 = color.equals(WHITE) ? Square.G1 : Square.G8;
        Square castlingSquare2 = color.equals(WHITE) ? Square.C1 : Square.C8;
        Square expectedKingSquare = color.equals(WHITE) ? Square.E1 : Square.E8;
        Square expectedRook1Square = color.equals(WHITE) ? Square.H1 : Square.H8;
        Square expectedRook2Square = color.equals(WHITE) ? Square.A1 : Square.A8;

        boolean isKingEligible = isRequiredPieceOnSquareAndDidNotMove(expectedKingSquare, King.class, piecesLocations, moveHistory);
        if (!isKingEligible) {
            return result;
        }
        boolean isRook1Eligible = isRequiredPieceOnSquareAndDidNotMove(expectedRook1Square, Rook.class, piecesLocations, moveHistory);
        boolean isRook2Eligible = isRequiredPieceOnSquareAndDidNotMove(expectedRook2Square, Rook.class, piecesLocations, moveHistory);
        if (!isRook1Eligible && !isRook2Eligible) {
            return result;
        }
        boolean possibleToCastleBySiteOfRook1 = isPossibleToCastleBySite(expectedKingSquare, expectedRook1Square, piecesLocations, lastPerformedMove);
        boolean possibleToCastleBySiteOfRook2 = isPossibleToCastleBySite(expectedKingSquare, expectedRook2Square, piecesLocations, lastPerformedMove);

        if (isRook1Eligible && possibleToCastleBySiteOfRook1) {
            result.add(castlingSquare1);
        }
        if (isRook2Eligible && possibleToCastleBySiteOfRook2) {
            result.add(castlingSquare2);
        }
        return result;
    }

    private boolean isRequiredPieceOnSquareAndDidNotMove(
        Square expectedSquare,
        Class<? extends Piece> expectedPieceType,
        PiecesLocations piecesLocations,
        MoveHistory moveHistory
    ) {
        Optional<Piece> pieceOnKingSquare = piecesLocations.findPieceOnSquare(expectedSquare);
        return pieceOnKingSquare.isPresent()
            && (pieceOnKingSquare.get().getClass().equals(expectedPieceType))
            && pieceOnKingSquare.get().color().equals(color)
            && !moveHistory.didPieceMadeMove(pieceOnKingSquare.get());
    }

    private boolean isPossibleToCastleBySite(
        Square kingSquare,
        Square castlingSquare,
        PiecesLocations piecesLocations,
        Move lastPerformedMove
    ) {
        Set<Square> expectedEmptySquares;
        if (castlingSquare.getFile().equals(SquareFile.A)) {
            expectedEmptySquares = color.equals(WHITE)
                ? Set.of(Square.B1, Square.C1, Square.D1)
                : Set.of(Square.B8, Square.C8, Square.D8);
        } else {
            expectedEmptySquares = color.equals(WHITE)
                ? Set.of(Square.F1, Square.G1)
                : Set.of(Square.F8, Square.G8);
        }

        boolean areExpectedSquaresEmpty = expectedEmptySquares.stream().allMatch(square -> piecesLocations.findPieceOnSquare(square).isEmpty());
        Set<Square> expectedNonAttackedSquares = Stream.concat(
                expectedEmptySquares.stream(),
                Stream.of(kingSquare, castlingSquare)
            )
            .collect(Collectors.toSet());

        boolean isKingUnderCheckOrWillBeAfterCastling = piecesLocations.getEnemyPieces(color)
            .stream()
            .anyMatch(
                piece -> piece.isAttackingAnyOfSquares(
                    expectedNonAttackedSquares,
                    piecesLocations,
                    lastPerformedMove
                )
            );


        return areExpectedSquaresEmpty && !isKingUnderCheckOrWillBeAfterCastling;
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
