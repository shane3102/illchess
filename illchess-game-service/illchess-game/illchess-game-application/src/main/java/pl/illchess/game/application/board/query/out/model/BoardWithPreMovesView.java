package pl.illchess.game.application.board.query.out.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record BoardWithPreMovesView(
    UUID boardId,
    Map<String, PieceView> piecesLocations,
    MoveView lastPerformedMove,
    List<MoveView> preMoves,
    String whiteUsername,
    String blackUsername
) {
}
