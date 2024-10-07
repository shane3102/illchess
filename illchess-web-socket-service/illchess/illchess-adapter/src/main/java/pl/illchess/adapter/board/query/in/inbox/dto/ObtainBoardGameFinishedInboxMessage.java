package pl.illchess.adapter.board.query.in.inbox.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import pl.messaging.model.Message;

public class ObtainBoardGameFinishedInboxMessage extends Message {

    private final UUID gameId;

    public ObtainBoardGameFinishedInboxMessage() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        gameId = null;
    }

    public ObtainBoardGameFinishedInboxMessage(UUID gameId) {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        this.gameId = gameId;
    }

    public UUID gameId() {
        return gameId;
    }
}
