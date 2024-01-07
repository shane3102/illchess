package pl.illchess.application.board.query.out.model;

import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.Map;
import java.util.UUID;

public record BoardView(
        UUID boardId,
        Map<Square, PieceView> piecesLocations,
        PieceColor currentPlayerColor
) {
    public record PieceView(PieceColor color, String type) {

    }
}
