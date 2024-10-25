package pl.illchess.game.domain.board.command;

import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.board.model.state.player.Username;
import pl.illchess.game.domain.piece.model.info.PieceType;

public record MovePiece(
        BoardId boardId,
        Square startSquare,
        Square targetSquare,
        PieceType pawnPromotedToPieceType,
        Username username
) {
}
