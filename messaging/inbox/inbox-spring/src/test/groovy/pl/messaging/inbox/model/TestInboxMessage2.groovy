package pl.messaging.inbox.model


import java.time.OffsetDateTime

class TestInboxMessage2 extends InboxMessage {
    TestInboxMessage2() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }
}
