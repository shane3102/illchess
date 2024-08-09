package pl.messaging.exception

import pl.messaging.model.Message

class InboxOutboxMethodShouldContainExactlyOneInboxOutboxMessageParameterException : RuntimeException(
    "Inbox-outbox message method should contain exactly one parameter of %s type".format(Message::class)
)