package pl.illchess.game.adapter.game.command.in.inbox.dto;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;
import pl.shane3102.messaging.model.Message;

@Getter
public class GameFinishedInboxMessage extends Message {

    private final UUID gameId;

    public GameFinishedInboxMessage(UUID gameId) {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        this.gameId = gameId;
    }

}
