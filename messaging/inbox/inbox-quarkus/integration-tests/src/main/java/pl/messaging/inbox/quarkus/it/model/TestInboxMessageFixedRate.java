package pl.messaging.inbox.quarkus.it.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import pl.messaging.inbox.model.InboxMessage;

public class TestInboxMessageFixedRate extends InboxMessage {
    public TestInboxMessageFixedRate() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
    }
}
