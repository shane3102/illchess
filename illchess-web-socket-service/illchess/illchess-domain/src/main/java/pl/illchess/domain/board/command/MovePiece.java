package pl.illchess.domain.board.command;

import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.state.player.Username;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.info.PieceType;

public record MovePiece(
        BoardId boardId,
        Square startSquare,
        Square targetSquare,
        PieceType pawnPromotedToPieceType,
        Username username
) {
}
