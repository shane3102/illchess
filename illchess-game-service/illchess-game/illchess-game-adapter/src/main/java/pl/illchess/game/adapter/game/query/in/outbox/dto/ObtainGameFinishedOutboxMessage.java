package pl.illchess.game.adapter.game.query.in.outbox.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import pl.shane3102.messaging.model.Message;

public class ObtainGameFinishedOutboxMessage extends Message {

    private final UUID gameId;

    public ObtainGameFinishedOutboxMessage() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        gameId = null;
    }

    public ObtainGameFinishedOutboxMessage(UUID gameId) {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        this.gameId = gameId;
    }

    public UUID gameId() {
        return gameId;
    }
}
