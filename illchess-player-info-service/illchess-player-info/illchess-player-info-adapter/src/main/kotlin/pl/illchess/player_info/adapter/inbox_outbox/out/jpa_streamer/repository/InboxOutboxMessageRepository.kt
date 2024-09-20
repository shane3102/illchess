package pl.illchess.player_info.adapter.inbox_outbox.out.jpa_streamer.repository

import com.speedment.jpastreamer.application.JPAStreamer
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID
import pl.illchess.player_info.adapter.shared_entities.InboxOutboxMessageEntity
import pl.illchess.player_info.adapter.shared_entities.InboxOutboxMessageEntityMetaModel

@ApplicationScoped
class InboxOutboxMessageRepository(
    private val jpaStreamer: JPAStreamer
) : PanacheRepositoryBase<InboxOutboxMessageEntity, UUID> {

    fun loadLatestByTypeNonExpired(
        className: String,
        batchSize: Int,
        maxRetryCount: Int
    ): List<InboxOutboxMessageEntity> {
        return jpaStreamer.stream(InboxOutboxMessageEntity::class.java)
            .filter(
                InboxOutboxMessageEntityMetaModel.retryCount.lessOrEqual(maxRetryCount)
                    .and(InboxOutboxMessageEntityMetaModel.className.contains(className))
            )
            .limit(batchSize.toLong())
            .toList()
    }

    fun save(entity: InboxOutboxMessageEntity) {
        if (this.findById(entity.id) != null) {
            this.entityManager.merge(entity)
        } else {
            entity.persist()
        }
    }
}