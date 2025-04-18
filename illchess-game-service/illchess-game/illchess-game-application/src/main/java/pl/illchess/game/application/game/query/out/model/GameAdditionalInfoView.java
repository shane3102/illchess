package pl.illchess.game.application.game.query.out.model;

import java.util.List;
import java.util.UUID;

public record GameAdditionalInfoView(
    UUID gameId,
    String currentPlayerColor,
    PlayerView whitePlayer,
    PlayerView blackPlayer,
    String gameState,
    String gameResultCause,
    List<String> capturedWhitePieces,
    List<String> capturedBlackPieces,
    List<String> performedMoves
) {
}
