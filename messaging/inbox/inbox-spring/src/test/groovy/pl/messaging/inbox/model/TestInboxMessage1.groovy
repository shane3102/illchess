package pl.messaging.inbox.model

import org.jetbrains.annotations.NotNull

import java.time.OffsetDateTime

class TestInboxMessage1 extends InboxMessage {

    TestInboxMessage1(@NotNull UUID id) {
        super(id, OffsetDateTime.now())
    }

    @Override
    String className() {
        return TestInboxMessage1.class
    }
}