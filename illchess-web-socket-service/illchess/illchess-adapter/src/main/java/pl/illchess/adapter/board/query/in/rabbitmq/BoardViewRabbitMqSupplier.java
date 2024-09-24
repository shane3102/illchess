package pl.illchess.adapter.board.query.in.rabbitmq;

import org.springframework.context.event.EventListener;
import pl.illchess.application.board.query.out.model.BoardGameFinishedView;
import pl.illchess.domain.board.event.GameFinished;

public interface BoardViewRabbitMqSupplier {

    @EventListener(GameFinished.class)
    BoardGameFinishedView gameFinishedSupplier(GameFinished gameFinished);
}
