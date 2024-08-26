package pl.illchess.player_info.adapter.commons.command.out

import io.vertx.mutiny.core.eventbus.EventBus
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.application.commons.command.out.PublishEvent
import pl.illchess.player_info.domain.commons.event.DomainEvent

@ApplicationScoped
class EventPublisher(private val eventBus: EventBus) : PublishEvent {

    override fun publish(event: DomainEvent) {
        eventBus.publish(event::class.toString(), event)
    }
}