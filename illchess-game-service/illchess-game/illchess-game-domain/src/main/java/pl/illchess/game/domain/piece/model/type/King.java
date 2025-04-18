package pl.illchess.game.domain.piece.model.type;

import pl.illchess.game.domain.game.model.history.Move;
import pl.illchess.game.domain.game.model.history.MoveHistory;
import pl.illchess.game.domain.game.model.square.Board;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.square.info.SquareFile;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.info.PieceAttackingRay;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.info.PieceType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.illchess.game.domain.piece.model.info.PieceColor.WHITE;

public final class King implements Piece {
    private final PieceColor color;
    private Square square;
    private Set<Square> cachedReachableSquares;

    public King(
        PieceColor color,
        Square square
    ) {
        this.color = color;
        this.square = square;
        this.cachedReachableSquares = Set.of();
    }

    public King(PieceColor color, Square square, Set<Square> cachedReachableSquares) {
        this.color = color;
        this.square = square;
        this.cachedReachableSquares = cachedReachableSquares;
    }

    @Override
    public Set<Square> standardLegalMoves(Board board, Move lastPerformedMove) {
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
    public Set<Square> possibleMovesOnPreMove(Board board, MoveHistory moveHistory) {
        return Stream.concat(
                standardLegalMoves(board, moveHistory.peekLastMove()).stream(),
                getCastlingSquares(board, moveHistory).stream()
            )
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Square> possibleMoves(Board board, MoveHistory moveHistory) {
        Move lastPerformedMove = moveHistory.peekLastMove();

        Set<Square> standardKingMovement = standardLegalMoves(board, lastPerformedMove);
        Set<Square> castlingSquares = getCastlingSquares(board, moveHistory);

        Set<Square> standardMovesWithCastlingMoves = Stream.of(standardKingMovement, castlingSquares)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        Set<Square> alliedOccupiedSquares = board.getAlliedPieces(color())
            .stream()
            .map(Piece::square)
            .collect(Collectors.toSet());

        return standardMovesWithCastlingMoves.stream()
            .filter(square -> !isEnemyAttackingSquare(square, board, lastPerformedMove))
            .filter(square -> alliedOccupiedSquares.isEmpty() || !alliedOccupiedSquares.contains(square))
            .collect(Collectors.toSet());
    }

    @Override
    public PieceAttackingRay attackingRayOfSquare(Square possibleAttackedSquare, Board board, Move lastPerformedMove) {
        Set<Square> attackingRay = standardLegalMoves(board, lastPerformedMove).stream()
            .filter(checkedSquare -> Objects.equals(checkedSquare.name(), possibleAttackedSquare.name()))
            .collect(Collectors.toSet());
        return new PieceAttackingRay(square, attackingRay);
    }

    private boolean isEnemyAttackingSquare(
        Square possibleAttackedSquare,
        Board board,
        Move lastPerformedMove
    ) {
        Set<Piece> enemyPieces = board.getEnemyPieces(color);

        return enemyPieces
            .stream()
            .anyMatch(
                piece -> {
                    Set<Square> fullRay = piece.attackingRayOfSquare(possibleAttackedSquare, board, lastPerformedMove)
                        .rayUntilPieceEncounteredWithoutOccupiedSquare();
                    return fullRay.contains(possibleAttackedSquare);
                }
            );
    }

    public Set<Square> getCastlingSquares(Board board, MoveHistory moveHistory) {
        Set<Square> result = new HashSet<>();

        Move lastPerformedMove = moveHistory.peekLastMove();

        Square castlingSquare1 = color.equals(WHITE) ? Square.G1 : Square.G8;
        Square castlingSquare2 = color.equals(WHITE) ? Square.C1 : Square.C8;
        Square expectedKingSquare = color.equals(WHITE) ? Square.E1 : Square.E8;
        Square expectedRook1Square = color.equals(WHITE) ? Square.H1 : Square.H8;
        Square expectedRook2Square = color.equals(WHITE) ? Square.A1 : Square.A8;

        boolean isKingEligible = isRequiredPieceOnSquareAndDidNotMove(expectedKingSquare, King.class, board, moveHistory);
        if (!isKingEligible) {
            return result;
        }
        boolean isRook1Eligible = isRequiredPieceOnSquareAndDidNotMove(expectedRook1Square, Rook.class, board, moveHistory);
        boolean isRook2Eligible = isRequiredPieceOnSquareAndDidNotMove(expectedRook2Square, Rook.class, board, moveHistory);
        if (!isRook1Eligible && !isRook2Eligible) {
            return result;
        }
        boolean possibleToCastleBySiteOfRook1 = isPossibleToCastleBySite(expectedKingSquare, expectedRook1Square, board, lastPerformedMove);
        boolean possibleToCastleBySiteOfRook2 = isPossibleToCastleBySite(expectedKingSquare, expectedRook2Square, board, lastPerformedMove);

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
        Board board,
        MoveHistory moveHistory
    ) {
        Optional<Piece> pieceOnKingSquare = board.findPieceOnSquare(expectedSquare);
        return pieceOnKingSquare.isPresent()
            && (pieceOnKingSquare.get().getClass().equals(expectedPieceType))
            && pieceOnKingSquare.get().color().equals(color)
            && !moveHistory.didPieceMadeMove(pieceOnKingSquare.get());
    }

    private boolean isPossibleToCastleBySite(
        Square kingSquare,
        Square castlingSquare,
        Board board,
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

        boolean areExpectedSquaresEmpty = expectedEmptySquares.stream().allMatch(square -> board.findPieceOnSquare(square).isEmpty());
        Set<Square> expectedNonAttackedSquares = Stream.concat(
                expectedEmptySquares.stream().filter(square -> !Square.B1.equals(square) && !Square.B8.equals(square)),
                Stream.of(kingSquare)
            )
            .collect(Collectors.toSet());

        boolean isKingUnderCheckOrWillBeAfterCastling = board.getEnemyPieces(color)
            .stream()
            .anyMatch(
                piece -> piece.isAttackingAnyOfSquares(
                    expectedNonAttackedSquares,
                    board,
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

    @Override
    public Set<Square> cachedReachableSquares() {
        return cachedReachableSquares;
    }

    @Override
    public void setCachedReachableSquares(Set<Square> cachedReachableSquares) {
        this.cachedReachableSquares = cachedReachableSquares;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public PieceType typeName() {
        return new PieceType("KING");
    }

    @Override
    public Piece clonePiece() {
        return new King(color, square);
    }
}
