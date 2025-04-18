package pl.illchess.game.domain.game.command;

import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.piece.model.info.PieceType;

public record MovePiece(
        GameId gameId,
        Square startSquare,
        Square targetSquare,
        PieceType pawnPromotedToPieceType,
        Username username
) {
}
