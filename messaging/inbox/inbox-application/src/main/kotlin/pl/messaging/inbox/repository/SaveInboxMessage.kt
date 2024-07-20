package pl.messaging.inbox.repository

import pl.messaging.inbox.model.InboxMessage

interface SaveInboxMessage {
    fun saveInboxMessage(inboxMessage: InboxMessage)
}