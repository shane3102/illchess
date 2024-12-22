package pl.illchess.player_info.adapter.inbox_outbox.out.jpa_streamer.repository.mapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import lombok.SneakyThrows
import pl.illchess.player_info.adapter.inbox_outbox.out.jpa_streamer.repository.mapper.mixin.InboxOutboxMessageMixin
import pl.illchess.player_info.adapter.shared_entities.InboxOutboxMessageEntity
import pl.shane3102.messaging.model.Message

@ApplicationScoped
class InboxOutboxMessageMapper {

    private val objectMapper: ObjectMapper

    init {
        val om = jacksonObjectMapper()
        om.registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        om.addMixIn(Message::class.java, InboxOutboxMessageMixin::class.java)
        this.objectMapper = om
    }

    fun mapToMessage(entity: InboxOutboxMessageEntity) = map(entity.getNode(), Message::class.java)

    fun mapToEntity(message: Message): InboxOutboxMessageEntity {
        val node: JsonNode = objectMapper.valueToTree(message)
        return InboxOutboxMessageEntity(
            message.id,
            message.retryCount,
            message.occurredOn,
            message::class.toString(),
            node
        )
    }

    @SneakyThrows
    private fun <T> map(o: Any, destination: Class<out T>): T {
        val json = objectMapper.writeValueAsString(o)
        return objectMapper.readValue(json, destination)
    }
}