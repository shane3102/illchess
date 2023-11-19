package pl.illchess.domain.board.model.history;

import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.Piece;

public record Move(
        Square startSquare,
        Square targetSquare,
        Piece movedPiece,
        Piece capturedPiece
) {
}