package pl.illchess.game.adapter.inbox_outbox.out.redis.mapper;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.illchess.game.adapter.inbox_outbox.out.redis.mapper.mixin.InboxOutboxMessageMixin;
import pl.illchess.game.adapter.inbox_outbox.out.redis.model.InboxOutboxMessageEntity;
import pl.messaging.model.Message;

@Component
public class InboxOutboxMessageMapper {

    private final ObjectMapper objectMapper;

    public InboxOutboxMessageMapper() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        om.addMixIn(Message.class, InboxOutboxMessageMixin.class);

        this.objectMapper = om;
    }

    public Message mapToMessage(InboxOutboxMessageEntity entity) {
        return map(entity.content(), Message.class);
    }

    public InboxOutboxMessageEntity mapToEntity(Message message) {
        JsonNode node = objectMapper.valueToTree(message);
        return new InboxOutboxMessageEntity(
            message.getId(),
            message.getRetryCount(),
            message.getOccurredOn(),
            message.getClass().toString(),
            node
        );
    }

    @SneakyThrows
    private <T> T map(Object o, Class<? extends T> destination) {
        String json = objectMapper.writeValueAsString(o);
        return objectMapper.readValue(json, destination);
    }
}
