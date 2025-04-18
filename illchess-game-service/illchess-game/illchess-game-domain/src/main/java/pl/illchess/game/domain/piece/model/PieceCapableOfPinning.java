package pl.illchess.game.domain.piece.model;

import pl.illchess.game.domain.game.model.square.Board;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.model.type.King;

import java.util.Set;

public interface PieceCapableOfPinning extends Piece {

    Set<Square> pinningRayIfImPinning(Piece askingPiece, King enemyKing, Board board);
}
