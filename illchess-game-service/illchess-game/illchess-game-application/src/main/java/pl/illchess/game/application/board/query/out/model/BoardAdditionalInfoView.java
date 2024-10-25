package pl.illchess.game.application.board.query.out.model;

import java.util.List;
import java.util.UUID;

public record BoardAdditionalInfoView(
    UUID boardId,
    String currentPlayerColor,
    PlayerView whitePlayer,
    PlayerView blackPlayer,
    String gameState,
    String victoriousPlayerColor,
    List<String> capturedWhitePieces,
    List<String> capturedBlackPieces,
    List<String> performedMoves
) {
}
