package pl.illchess.game.adapter.inbox_outbox.out.redis.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

public record InboxOutboxMessageEntity(
    UUID id,
    int retryCount,
    OffsetDateTime occurredOn,
    String className,
    JsonNode content
) implements Serializable {

}
