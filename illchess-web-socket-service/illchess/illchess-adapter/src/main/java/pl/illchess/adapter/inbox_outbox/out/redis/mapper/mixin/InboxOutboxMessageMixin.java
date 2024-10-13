package pl.illchess.adapter.inbox_outbox.out.redis.mapper.mixin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.illchess.adapter.board.query.in.outbox.dto.ObtainBoardGameFinishedOutboxMessage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes(
    value = {
        @JsonSubTypes.Type(value = ObtainBoardGameFinishedOutboxMessage.class, name = "ObtainBoardGameFinishedInboxMessage")
    }
)
public class InboxOutboxMessageMixin {
}
