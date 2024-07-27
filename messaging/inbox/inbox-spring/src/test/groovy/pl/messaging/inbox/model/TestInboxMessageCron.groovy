package pl.messaging.inbox.model

import java.time.OffsetDateTime

class TestInboxMessageCron extends InboxMessage {
    TestInboxMessageCron() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }
}
