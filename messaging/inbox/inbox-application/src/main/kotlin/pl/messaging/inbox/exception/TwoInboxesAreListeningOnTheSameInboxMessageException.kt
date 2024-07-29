package pl.messaging.inbox.exception

import pl.messaging.inbox.model.InboxMessage
import java.lang.RuntimeException

class TwoInboxesAreListeningOnTheSameInboxMessageException(type: Class<out InboxMessage>) : RuntimeException(
    "Two inboxes are listening on same inbox message type (%s). " +
            "Create separate types for each inbox listener or aggregate operations into one inbox listener"
                .format(type)
)