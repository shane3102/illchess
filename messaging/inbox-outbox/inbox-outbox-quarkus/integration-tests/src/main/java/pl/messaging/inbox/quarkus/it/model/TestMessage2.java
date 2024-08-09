package pl.messaging.inbox.quarkus.it.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import pl.messaging.model.Message;

public class TestMessage2 extends Message {
    public TestMessage2() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now());
    }
}
