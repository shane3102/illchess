package pl.messaging.inbox.model

import java.time.OffsetDateTime
import java.util.UUID

abstract class InboxMessage(
    val id: UUID,
    var retryCount: Int = 0,
    val occurredOn: OffsetDateTime = OffsetDateTime.now(),
) {
    fun incrementCount() {
        retryCount++
    }
}