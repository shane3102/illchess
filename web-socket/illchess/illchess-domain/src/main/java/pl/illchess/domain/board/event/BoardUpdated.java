package pl.illchess.domain.board.event;

import pl.illchess.domain.commons.event.DomainEvent;

import java.util.UUID;

public record BoardUpdated(UUID boardId) implements DomainEvent {
}
