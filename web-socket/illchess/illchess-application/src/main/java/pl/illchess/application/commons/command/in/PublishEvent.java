package pl.illchess.application.commons.command.in;

import pl.illchess.domain.commons.event.DomainEvent;

public interface PublishEvent {
    void publishDomainEvent(DomainEvent domainEvent);
}
