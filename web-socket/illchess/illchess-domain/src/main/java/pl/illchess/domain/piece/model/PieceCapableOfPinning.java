package pl.illchess.domain.piece.model;

import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.type.King;

import java.util.Set;

public abstract class PieceCapableOfPinning extends Piece {

    public abstract Set<Square> pinningRayIfImPinning(Piece askingPiece, King enemyKing, PiecesLocations piecesLocations);
}
