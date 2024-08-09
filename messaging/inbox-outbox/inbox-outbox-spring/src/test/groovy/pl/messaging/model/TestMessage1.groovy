package pl.messaging.model

import java.time.OffsetDateTime

class TestMessage1 extends Message {
    TestMessage1() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }
}
