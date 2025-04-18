package pl.illchess.game.domain.piece.model;

import pl.illchess.game.domain.game.model.history.Move;
import pl.illchess.game.domain.game.model.history.MoveHistory;
import pl.illchess.game.domain.game.model.square.Board;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.exception.KingNotFoundOnBoardException;
import pl.illchess.game.domain.piece.model.info.PieceAttackingRay;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.info.PieceType;
import pl.illchess.game.domain.piece.model.type.King;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface Piece {

    PieceColor color();

    Square square();

    Set<Square> cachedReachableSquares();

    default Set<Square> possibleMoves(
        Board board,
        MoveHistory moveHistory
    ) {
        if (cachedReachableSquares() != null && !cachedReachableSquares().isEmpty()) {
            return cachedReachableSquares();
        }

        Move lastPerformedMove = moveHistory.peekLastMove();

        Set<Square> reachableSquares = standardLegalMoves(board, lastPerformedMove);

        King king = (King) board.getPieceByTypeAndColor(King.class, color())
            .orElseThrow(KingNotFoundOnBoardException::new);

        Set<PieceCapableOfPinning> enemyPiecesCapableOfPinning = board.getEnemyPiecesCapableOfPinning(color());
        Set<Square> availableSquaresAsPinnedPiece = enemyPiecesCapableOfPinning.stream()
            .map(piece -> piece.pinningRayIfImPinning(this, king, board))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        Set<Piece> enemyPieces = board.getEnemyPieces(color());
        Set<Square> kingDefendingSquareMoves = enemyPieces.stream()
            .map(piece -> piece.attackingRayOfSquare(king.square(), board, lastPerformedMove))
            .filter(foundRay -> !foundRay.squaresInRay().isAttackingRayEmpty())
            .map(PieceAttackingRay::fullAttackingRayWithOccupiedSquare)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        Set<Square> alliedOccupiedSquares = board.getAlliedPieces(color())
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
        Board board,
        MoveHistory moveHistory
    ) {
        return standardLegalMoves(board, moveHistory.peekLastMove());
    }

    Set<Square> standardLegalMoves(
        Board board,
        Move lastPerformedMove
    );

    PieceType typeName();

    PieceAttackingRay attackingRayOfSquare(Square possibleAttackedSquare, Board board, Move lastPerformedMove);

    default boolean isAttackingSquare(Square possibleAttackedSquare, Board board, Move lastPerformedMove) {
        return !attackingRayOfSquare(possibleAttackedSquare, board, lastPerformedMove).squaresInRay().isAttackingRayEmpty();
    }

    default boolean isAttackingAnyOfSquares(Set<Square> possibleAttackedSquares, Board board, Move lastPerformedMove) {
        return possibleAttackedSquares.stream().anyMatch(square -> isAttackingSquare(square, board, lastPerformedMove));
    }

    default void resetCachedReachableSquares() {
        setCachedReachableSquares(Set.of());
    }

    void setSquare(Square square);

    void setCachedReachableSquares(Set<Square> squares);

    Piece clonePiece();

}
