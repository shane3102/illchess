package pl.messaging.inbox.model


import java.time.OffsetDateTime

class TestInboxMessageFixedDelay extends InboxMessage {
    TestInboxMessageFixedDelay() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }
}
