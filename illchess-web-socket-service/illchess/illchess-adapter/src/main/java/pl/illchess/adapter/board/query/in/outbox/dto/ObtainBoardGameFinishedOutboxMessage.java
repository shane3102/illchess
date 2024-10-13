package pl.illchess.adapter.board.query.in.outbox.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import pl.messaging.model.Message;

public class ObtainBoardGameFinishedOutboxMessage extends Message {

    private final UUID gameId;

    public ObtainBoardGameFinishedOutboxMessage() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        gameId = null;
    }

    public ObtainBoardGameFinishedOutboxMessage(UUID gameId) {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        this.gameId = gameId;
    }

    public UUID gameId() {
        return gameId;
    }
}
