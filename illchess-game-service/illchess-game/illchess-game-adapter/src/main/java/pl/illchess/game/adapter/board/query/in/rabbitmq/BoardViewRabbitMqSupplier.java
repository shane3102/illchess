package pl.illchess.game.adapter.board.query.in.rabbitmq;

import org.springframework.context.event.EventListener;
import pl.illchess.game.application.game.query.out.model.BoardGameFinishedView;
import pl.illchess.game.domain.game.event.GameFinished;

public interface BoardViewRabbitMqSupplier {

    @EventListener(GameFinished.class)
    BoardGameFinishedView gameFinishedSupplier(GameFinished gameFinished);
}
