package pl.illchess.domain.board.model.history;

import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.PieceBehaviour;

public record Move(
        Square startSquare,
        Square targetSquare,
        PieceBehaviour movedPiece,
        PieceBehaviour capturedPiece
) {
}
