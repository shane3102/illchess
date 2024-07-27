package pl.messaging.inbox.repository

import pl.messaging.inbox.model.InboxMessage

interface LoadInboxMessages {
    fun loadLatestByTypeNonExpired(className: String, batchSize: Int, maxRetryCount: Int): List<InboxMessage>
}