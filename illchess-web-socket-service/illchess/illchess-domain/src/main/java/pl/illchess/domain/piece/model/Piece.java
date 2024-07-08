package pl.illchess.domain.piece.model;

import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.exception.KingNotFoundOnBoardException;
import pl.illchess.domain.piece.model.info.PieceAttackingRay;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;
import pl.illchess.domain.piece.model.type.King;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface Piece {

    PieceColor color();

    Square square();

    Set<Square> cachedReachableSquares();

    default Set<Square> possibleMoves(
        PiecesLocations piecesLocations,
        MoveHistory moveHistory
    ) {
        if (cachedReachableSquares() != null && !cachedReachableSquares().isEmpty()) {
            return cachedReachableSquares();
        }

        Move lastPerformedMove = moveHistory.peekLastMove();

        Set<Square> reachableSquares = standardLegalMoves(piecesLocations, lastPerformedMove);

        King king = (King) piecesLocations.getPieceByTypeAndColor(King.class, color())
            .orElseThrow(KingNotFoundOnBoardException::new);

        Set<PieceCapableOfPinning> enemyPiecesCapableOfPinning = piecesLocations.getEnemyPiecesCapableOfPinning(color());
        Set<Square> availableSquaresAsPinnedPiece = enemyPiecesCapableOfPinning.stream()
            .map(piece -> piece.pinningRayIfImPinning(this, king, piecesLocations))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        Set<Piece> enemyPieces = piecesLocations.getEnemyPieces(color());
        Set<Square> kingDefendingSquareMoves = enemyPieces.stream()
            .map(piece -> piece.attackingRayOfSquare(king.square(), piecesLocations, lastPerformedMove))
            .filter(foundRay -> !foundRay.squaresInRay().isAttackingRayEmpty())
            .map(PieceAttackingRay::fullAttackingRayWithOccupiedSquare)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        Set<Square> alliedOccupiedSquares = piecesLocations.getAlliedPieces(color())
            .stream()
            .map(Piece::square)
            .collect(Collectors.toSet());

        Set<Square> result = reachableSquares.stream()
            .filter(square -> availableSquaresAsPinnedPiece.isEmpty() || availableSquaresAsPinnedPiece.contains(square))
            .filter(square -> kingDefendingSquareMoves.isEmpty() || kingDefendingSquareMoves.contains(square))
            .filter(square -> alliedOccupiedSquares.isEmpty() || !alliedOccupiedSquares.contains(square))
            .collect(Collectors.toSet());

        setCachedReachableSquares(result);

        return result;
    }

    default Set<Square> possibleMovesOnPreMove(
        PiecesLocations piecesLocations,
        MoveHistory moveHistory
    ) {
        return standardLegalMoves(piecesLocations, moveHistory.peekLastMove());
    }

    Set<Square> standardLegalMoves(
        PiecesLocations piecesLocations,
        Move lastPerformedMove
    );

    PieceType typeName();

    PieceAttackingRay attackingRayOfSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations, Move lastPerformedMove);

    default boolean isAttackingSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations, Move lastPerformedMove) {
        return !attackingRayOfSquare(possibleAttackedSquare, piecesLocations, lastPerformedMove).squaresInRay().isAttackingRayEmpty();
    }

    default boolean isAttackingAnyOfSquares(Set<Square> possibleAttackedSquares, PiecesLocations piecesLocations, Move lastPerformedMove) {
        return possibleAttackedSquares.stream().anyMatch(square -> isAttackingSquare(square, piecesLocations, lastPerformedMove));
    }

    default void resetCachedReachableSquares() {
        setCachedReachableSquares(Set.of());
    }

    void setSquare(Square square);

    void setCachedReachableSquares(Set<Square> squares);

    Piece clonePiece();

}
