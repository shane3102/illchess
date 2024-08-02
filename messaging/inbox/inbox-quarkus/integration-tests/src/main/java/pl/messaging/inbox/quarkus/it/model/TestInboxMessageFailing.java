package pl.messaging.inbox.quarkus.it.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import pl.messaging.inbox.model.InboxMessage;

public class TestInboxMessageFailing extends InboxMessage {
    public TestInboxMessageFailing() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
    }

    public TestInboxMessageFailing(@NotNull UUID id) {
        super(id, 0, OffsetDateTime.now());
    }
}
