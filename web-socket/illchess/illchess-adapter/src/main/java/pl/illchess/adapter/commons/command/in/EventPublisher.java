package pl.illchess.adapter.commons.command.in;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.illchess.application.commons.command.in.PublishEvent;
import pl.illchess.domain.commons.event.DomainEvent;

@Service
@AllArgsConstructor
public class EventPublisher implements PublishEvent {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publishDomainEvent(DomainEvent domainEvent) {
        publisher.publishEvent(domainEvent);
    }
}
