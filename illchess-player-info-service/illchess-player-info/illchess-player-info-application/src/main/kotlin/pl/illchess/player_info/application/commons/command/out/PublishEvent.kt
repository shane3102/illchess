package pl.illchess.player_info.application.commons.command.out

import pl.illchess.player_info.domain.commons.event.DomainEvent

interface PublishEvent {
    fun publish(destination: String, event: DomainEvent)
}