package pl.illchess.application.board.query.out.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BoardGameFinishedView(
    UUID gameId,
    String whiteUsername,
    String blackUsername,
    String gameResult,
    LocalDateTime endTime,
    List<PerformedMovesGameFinishedView> performedMoves
) {
    public record PerformedMovesGameFinishedView(
        String startSquare,
        String endSquare,
        String stringValue,
        String color
    ) {

    }
}
