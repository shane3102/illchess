package pl.illchess.adapter.commons.command.in;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.illchess.application.commons.command.in.PublishEvent;
import pl.illchess.domain.commons.event.DomainEvent;

@Service
@AllArgsConstructor
public class EventPublisher implements PublishEvent {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    private final ApplicationEventPublisher publisher;

    @Override
    public void publishDomainEvent(DomainEvent domainEvent) {
        log.info("New event of type = {} is being published", domainEvent.getClass());
        publisher.publishEvent(domainEvent);
    }
}
