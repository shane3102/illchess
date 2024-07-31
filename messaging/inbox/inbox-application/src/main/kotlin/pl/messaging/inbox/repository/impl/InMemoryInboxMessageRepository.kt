package pl.messaging.inbox.repository.impl

import pl.messaging.inbox.model.InboxMessage
import pl.messaging.inbox.repository.DeleteInboxMessage
import pl.messaging.inbox.repository.LoadInboxMessages
import pl.messaging.inbox.repository.SaveInboxMessage
import java.util.*

class InMemoryInboxMessageRepository(
    private val repo: HashMap<UUID, InboxMessage> = HashMap()
) : LoadInboxMessages, SaveInboxMessage, DeleteInboxMessage {

    override fun loadLatestByTypeNonExpired(className: String, batchSize: Int, maxRetryCount: Int): List<InboxMessage> {
        return repo.values.sortedBy { it.occurredOn }
            .stream()
            .filter { inboxMessage -> inboxMessage::class.java.toString() == className }
            .filter { inboxMessage -> inboxMessage.retryCount < maxRetryCount }
            .limit(batchSize.toLong())
            .toList()
    }

    override fun saveInboxMessage(inboxMessage: InboxMessage) {
        repo[inboxMessage.id] = inboxMessage
    }

    override fun delete(id: UUID) {
        repo.remove(id)
    }


}