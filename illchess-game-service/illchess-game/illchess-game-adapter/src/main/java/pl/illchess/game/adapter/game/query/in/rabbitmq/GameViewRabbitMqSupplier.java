package pl.illchess.game.adapter.game.query.in.rabbitmq;

import org.springframework.context.event.EventListener;
import pl.illchess.game.application.game.query.out.model.GameFinishedView;
import pl.illchess.game.domain.game.event.GameFinished;

public interface GameViewRabbitMqSupplier {

    @EventListener(GameFinished.class)
    GameFinishedView gameFinishedSupplier(GameFinished gameFinished);
}
