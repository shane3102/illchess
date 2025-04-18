package pl.illchess.game.application.game.query.out.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GameWithPreMovesView(
    UUID gameId,
    Map<String, PieceView> piecesLocations,
    MoveView lastPerformedMove,
    List<MoveView> preMoves,
    String whiteUsername,
    String blackUsername
) {
}
