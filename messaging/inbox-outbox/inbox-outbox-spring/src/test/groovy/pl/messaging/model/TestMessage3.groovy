package pl.messaging.model


import java.time.OffsetDateTime

class TestMessage3 extends Message {

    TestMessage3() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }

}