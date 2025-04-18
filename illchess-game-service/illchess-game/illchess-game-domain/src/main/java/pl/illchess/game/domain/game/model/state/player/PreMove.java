package pl.illchess.game.domain.game.model.state.player;

import pl.illchess.game.domain.game.command.MovePiece;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Board;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.model.info.PieceType;

public record PreMove(
    Square startSquare,
    Square targetSquare,
    PieceType pawnPromotedToPieceType,
    Board boardAfterPreMove
) {
    public MovePiece toCommand(GameId gameId, Username username) {
        return new MovePiece(
            gameId,
            startSquare,
            targetSquare,
            pawnPromotedToPieceType,
            username
        );
    }
}
