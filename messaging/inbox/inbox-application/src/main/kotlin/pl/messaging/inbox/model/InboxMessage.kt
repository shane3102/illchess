package pl.messaging.inbox.model

import java.time.OffsetDateTime
import java.util.UUID

abstract class InboxMessage(
    val id: UUID,
    val occurredOn: OffsetDateTime = OffsetDateTime.now(),
) {
    abstract fun className(): String
}