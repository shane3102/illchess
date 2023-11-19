package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.Piece;

public record MovePiece(
        Piece movedPiece,
        Square currentSquare,
        Square targetSquare
) {
}
