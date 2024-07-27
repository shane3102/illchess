package pl.messaging.inbox.model


import java.time.OffsetDateTime

class TestInboxMessageFailing extends InboxMessage {
    TestInboxMessageFailing() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }

    TestInboxMessageFailing(UUID id) {
        super(id, 0, OffsetDateTime.now())
    }
}
