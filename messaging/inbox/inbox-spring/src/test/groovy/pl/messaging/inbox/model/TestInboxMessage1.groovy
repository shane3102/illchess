package pl.messaging.inbox.model


import java.time.OffsetDateTime

class TestInboxMessage1 extends InboxMessage {

    TestInboxMessage1() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }

}