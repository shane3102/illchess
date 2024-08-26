package pl.illchess.player_info.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.player_info.application.commons.command.out.PublishEvent
import pl.illchess.player_info.domain.commons.event.DomainEvent

class TestPublishEvent implements PublishEvent {
    @Override
    void publish(@NotNull DomainEvent event) {

    }
}
