package pl.illchess.domain.board.model.state.player;

import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.info.PieceType;

public record PreMove(
    Square startSquare,
    Square targetSquare,
    PieceType pawnPromotedToPieceType,
    PiecesLocations piecesLocationsAfterPreMove
) {
}
