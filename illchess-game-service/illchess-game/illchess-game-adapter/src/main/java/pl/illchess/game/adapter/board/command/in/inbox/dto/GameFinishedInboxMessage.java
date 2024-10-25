package pl.illchess.game.adapter.board.command.in.inbox.dto;

import lombok.Getter;
import pl.messaging.model.Message;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class GameFinishedInboxMessage extends Message {

    private final UUID boardId;

    public GameFinishedInboxMessage(UUID boardId) {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
        this.boardId = boardId;
    }

}
