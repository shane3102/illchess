package pl.illchess.application.board.query.out.model;

import java.util.Map;
import java.util.UUID;

public record BoardView(
        UUID boardId,
        Map<String, PieceView> piecesLocations,
        String currentPlayerColor,
        String gameState,
        String victoriousPlayerColor
) {
    public record PieceView(String color, String type) {

    }
}
