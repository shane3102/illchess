package pl.illchess.game.domain.board.model.state.player;

import pl.illchess.game.domain.board.command.MovePiece;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.square.PiecesLocations;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.piece.model.info.PieceType;

public record PreMove(
    Square startSquare,
    Square targetSquare,
    PieceType pawnPromotedToPieceType,
    PiecesLocations piecesLocationsAfterPreMove
) {
    public MovePiece toCommand(BoardId boardId, Username username) {
        return new MovePiece(
            boardId,
            startSquare,
            targetSquare,
            pawnPromotedToPieceType,
            username
        );
    }
}
