package pl.messaging.repository.impl

import pl.messaging.model.Message
import pl.messaging.repository.DeleteMessage
import pl.messaging.repository.LoadMessages
import pl.messaging.repository.SaveMessage
import java.util.UUID

class InMemoryInboxMessageRepository(
    private val repo: HashMap<UUID, Message> = HashMap()
) : LoadMessages, SaveMessage, DeleteMessage {

    override fun loadLatestByTypeNonExpired(className: String, batchSize: Int, maxRetryCount: Int): List<Message> {
        return repo.values.sortedBy { it.occurredOn }
            .stream()
            .filter { inboxMessage -> inboxMessage::class.java.toString() == className }
            .filter { inboxMessage -> inboxMessage.retryCount < maxRetryCount }
            .limit(batchSize.toLong())
            .toList()
    }

    override fun saveMessage(message: Message) {
        repo[message.id] = message
    }

    override fun delete(id: UUID) {
        repo.remove(id)
    }


}