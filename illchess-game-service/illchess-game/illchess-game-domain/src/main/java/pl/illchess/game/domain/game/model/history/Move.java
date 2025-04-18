package pl.illchess.game.domain.game.model.history;

import pl.illchess.game.domain.game.model.FenBoardString;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.piece.model.Piece;

public record Move(
    Square startSquare,
    Square targetSquare,
    Piece movedPiece,
    Piece capturedPiece,
    FenBoardString fenBoardStringBeforeMove
) {
}
