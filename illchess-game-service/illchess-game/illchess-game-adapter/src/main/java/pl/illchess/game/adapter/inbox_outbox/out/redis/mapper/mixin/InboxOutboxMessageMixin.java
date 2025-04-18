package pl.illchess.game.adapter.inbox_outbox.out.redis.mapper.mixin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.illchess.game.adapter.game.query.in.outbox.dto.ObtainGameFinishedOutboxMessage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes(
    value = {
        @JsonSubTypes.Type(value = ObtainGameFinishedOutboxMessage.class, name = "ObtainBoardGameFinishedInboxMessage")
    }
)
public class InboxOutboxMessageMixin {
}
