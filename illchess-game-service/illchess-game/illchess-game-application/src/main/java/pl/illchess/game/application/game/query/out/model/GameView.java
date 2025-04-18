package pl.illchess.game.application.game.query.out.model;

import java.util.Map;
import java.util.UUID;

public record GameView(
    UUID gameId,
    Map<String, PieceView> piecesLocations,
    MoveView lastPerformedMove,
    String whiteUsername,
    String blackUsername
) {

}
