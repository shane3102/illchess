package pl.illchess.domain.piece.model;

import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.type.King;

import java.util.Set;

public interface PieceCapableOfPinning extends Piece {

    Set<Square> pinningRayIfImPinning(Piece askingPiece, King enemyKing, PiecesLocations piecesLocations);
}
