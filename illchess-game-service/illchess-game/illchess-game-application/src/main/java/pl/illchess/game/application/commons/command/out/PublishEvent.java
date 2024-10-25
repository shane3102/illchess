package pl.illchess.game.application.commons.command.out;

import pl.illchess.game.domain.commons.event.DomainEvent;

public interface PublishEvent {
    void publishDomainEvent(DomainEvent domainEvent);
}
