package pl.messaging.model


import java.time.OffsetDateTime

class TestMessage2 extends Message {
    TestMessage2() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }
}
