package pl.illchess.application.board

import pl.illchess.application.commons.command.out.PublishEvent
import pl.illchess.domain.commons.event.DomainEvent

class TestEventPublisher implements PublishEvent {
    @Override
    void publishDomainEvent(DomainEvent domainEvent) {

    }
}
