package pl.illchess.domain.board.event;

import pl.illchess.domain.commons.event.DomainEvent;

import java.util.UUID;

public record MoveIllegal(UUID boardId, String highlightSquare, String message) implements DomainEvent {
}
