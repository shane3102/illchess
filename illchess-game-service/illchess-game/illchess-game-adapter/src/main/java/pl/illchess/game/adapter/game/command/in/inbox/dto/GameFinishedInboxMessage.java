package pl.illchess.game.adapter.game.command.in.inbox.dto;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;
import pl.shane3102.messaging.model.Message;

@Getter
public class GameFinishedInboxMessage extends Message {

    private final UUID boardId;

    public GameFinishedInboxMessage(UUID boardId) {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        this.boardId = boardId;
    }

}
