package pl.messaging.inbox.model


import java.time.OffsetDateTime

class TestInboxMessageFixedRate extends InboxMessage {

    TestInboxMessageFixedRate() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }

}