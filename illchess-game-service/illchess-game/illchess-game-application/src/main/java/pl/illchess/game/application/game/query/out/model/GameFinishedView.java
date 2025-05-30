package pl.illchess.game.application.game.query.out.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GameFinishedView(
    UUID gameId,
    String whiteUsername,
    String blackUsername,
    String gameResult,
    String gameResultCause,
    LocalDateTime endTime,
    List<PerformedMovesGameFinishedView> performedMoves
) implements Serializable {
    public record PerformedMovesGameFinishedView(
        String startSquare,
        String endSquare,
        String stringValue,
        String color
    ) implements Serializable {

    }
}
