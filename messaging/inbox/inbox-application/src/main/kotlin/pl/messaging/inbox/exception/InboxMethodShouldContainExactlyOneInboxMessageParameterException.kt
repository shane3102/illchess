package pl.messaging.inbox.exception

import pl.messaging.inbox.model.InboxMessage

class InboxMethodShouldContainExactlyOneInboxMessageParameterException : RuntimeException(
    "Inbox message method should contain exactly one parameter of %s type".format(InboxMessage::class)
)