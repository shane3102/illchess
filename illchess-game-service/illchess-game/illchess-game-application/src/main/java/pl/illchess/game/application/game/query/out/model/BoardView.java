package pl.illchess.game.application.game.query.out.model;

import java.util.Map;
import java.util.UUID;

public record BoardView(
        UUID boardId,
        Map<String, PieceView> piecesLocations,
        MoveView lastPerformedMove,
        String whiteUsername,
        String blackUsername
) {

}
