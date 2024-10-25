package pl.illchess.application.board

import pl.illchess.game.application.commons.command.out.PublishEvent
import pl.illchess.game.domain.commons.event.DomainEvent

class TestEventPublisher implements PublishEvent {
    @Override
    void publishDomainEvent(DomainEvent domainEvent) {

    }
}
