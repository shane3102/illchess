package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.Piece;

public record MovePiece(
        BoardId boardId,
        Piece movedPiece,
        Square currentSquare,
        Square targetSquare
) {
}
