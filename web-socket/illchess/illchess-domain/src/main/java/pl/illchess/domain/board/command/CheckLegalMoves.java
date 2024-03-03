package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.piece.model.Piece;

public record CheckLegalMoves(
    BoardId boardId,
    Piece movedPiece
) {
}
