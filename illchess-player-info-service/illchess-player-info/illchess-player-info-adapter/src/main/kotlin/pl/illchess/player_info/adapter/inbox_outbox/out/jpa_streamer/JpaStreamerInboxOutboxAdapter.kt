package pl.illchess.player_info.adapter.inbox_outbox.out.jpa_streamer

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.util.UUID
import pl.illchess.player_info.adapter.inbox_outbox.out.jpa_streamer.repository.InboxOutboxMessageRepository
import pl.illchess.player_info.adapter.inbox_outbox.out.jpa_streamer.repository.mapper.InboxOutboxMessageMapper
import pl.messaging.model.Message
import pl.messaging.repository.DeleteMessage
import pl.messaging.repository.LoadMessages
import pl.messaging.repository.SaveMessage

@ApplicationScoped
class JpaStreamerInboxOutboxAdapter(
    private val repository: InboxOutboxMessageRepository,
    private val mapper: InboxOutboxMessageMapper
) : LoadMessages, SaveMessage, DeleteMessage {

    @Transactional
    override fun delete(id: UUID) {
        repository.deleteById(id)
    }

    override fun loadLatestByTypeNonExpired(className: String, batchSize: Int, maxRetryCount: Int): List<Message> {
        return repository.loadLatestByTypeNonExpired(className, batchSize, maxRetryCount)
            .map { mapper.mapToMessage(it) }
    }

    @Transactional
    override fun saveMessage(message: Message) {
        val entity = mapper.mapToEntity(message)
        repository.save(entity)
    }
}