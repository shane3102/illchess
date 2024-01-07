package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.PieceBehaviour;

public record MovePiece(
        BoardId boardId,
        PieceBehaviour movedPiece,
        Square targetSquare
) {
}
