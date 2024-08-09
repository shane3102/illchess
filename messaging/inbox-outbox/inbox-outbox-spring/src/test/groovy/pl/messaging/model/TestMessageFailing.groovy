package pl.messaging.model


import java.time.OffsetDateTime

class TestMessageFailing extends Message {
    TestMessageFailing() {
        super(UUID.randomUUID(), 0, OffsetDateTime.now())
    }

    TestMessageFailing(UUID id) {
        super(id, 0, OffsetDateTime.now())
    }
}
