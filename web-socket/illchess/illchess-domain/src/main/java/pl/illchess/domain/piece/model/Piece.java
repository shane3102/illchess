package pl.illchess.domain.piece.model;

import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.exception.KingNotFoundOnBoardException;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;
import pl.illchess.domain.piece.model.type.King;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface Piece {

    PieceColor color();

    Square square();

    default Set<Square> possibleMoves(
            PiecesLocations piecesLocations,
            Move lastPerformedMove
    ) {
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
                .filter(foundRay -> !foundRay.isEmpty())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return reachableSquares.stream()
                .filter(square -> availableSquaresAsPinnedPiece.isEmpty() || availableSquaresAsPinnedPiece.contains(square))
                .filter(square -> kingDefendingSquareMoves.isEmpty() || kingDefendingSquareMoves.contains(square))
                .collect(Collectors.toSet());
    }

    Set<Square> standardLegalMoves(
            PiecesLocations piecesLocations,
            Move lastPerformedMove
    );

    PieceType typeName();

    Set<Square> attackingRayOfSquare(Square possibleAttackedSquare, PiecesLocations piecesLocations, Move lastPerformedMove);

    void setSquare(Square square);

}
