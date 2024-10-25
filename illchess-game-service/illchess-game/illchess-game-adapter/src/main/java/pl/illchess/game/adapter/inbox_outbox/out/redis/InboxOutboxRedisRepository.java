package pl.illchess.game.adapter.inbox_outbox.out.redis;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pl.illchess.game.adapter.inbox_outbox.out.redis.mapper.InboxOutboxMessageMapper;
import pl.illchess.game.adapter.inbox_outbox.out.redis.model.InboxOutboxMessageEntity;
import pl.messaging.model.Message;
import pl.messaging.repository.DeleteMessage;
import pl.messaging.repository.LoadMessages;
import pl.messaging.repository.SaveMessage;

@Repository
@RequiredArgsConstructor
public class InboxOutboxRedisRepository implements LoadMessages, SaveMessage, DeleteMessage {

    private static final String INBOX_OUTBOX_MESSAGE_HASH_KEY = "INBOX_OUTBOX_MESSAGE";

    private final InboxOutboxMessageMapper mapper;
    private final RedisTemplate<String, InboxOutboxMessageEntity> template;

    @Override
    public void delete(@NotNull UUID id) {
        template.opsForHash().delete(id.toString(), INBOX_OUTBOX_MESSAGE_HASH_KEY);
    }

    @NotNull
    @Override
    public List<Message> loadLatestByTypeNonExpired(@NotNull String className, int batchSize, int maxRetryCount) {
        return template.opsForHash().entries(INBOX_OUTBOX_MESSAGE_HASH_KEY).values()
            .stream()
            .map(it -> (InboxOutboxMessageEntity) it)
            .filter(it -> it.className().equals(className))
            .filter(it -> it.retryCount() <= maxRetryCount)
            .sorted(Comparator.comparing(InboxOutboxMessageEntity::occurredOn))
            .limit(batchSize)
            .map(mapper::mapToMessage)
            .toList();
    }

    @Override
    public void saveMessage(@NotNull Message message) {
        InboxOutboxMessageEntity entity = mapper.mapToEntity(message);
        template.opsForHash().put(INBOX_OUTBOX_MESSAGE_HASH_KEY, entity.id().toString(), entity);
    }
}
