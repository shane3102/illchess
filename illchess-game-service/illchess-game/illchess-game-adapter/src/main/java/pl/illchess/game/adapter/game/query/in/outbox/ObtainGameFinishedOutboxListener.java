package pl.illchess.game.adapter.game.query.in.outbox;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pl.illchess.game.adapter.game.query.in.outbox.dto.ObtainGameFinishedOutboxMessage;
import pl.illchess.game.application.game.query.out.GameFinishedQueryPort;
import pl.illchess.game.application.game.query.out.model.GameFinishedView;
import pl.illchess.game.domain.game.exception.GameNotFoundException;
import pl.shane3102.messaging.annotation.InboxOutboxListener;
import pl.shane3102.messaging.annotation.MessagingAwareComponent;
import static pl.illchess.game.adapter.game.query.in.rabbitmq.GameInfoRabbitMqSupplier.OBTAIN_GAME_QUEUE;

@Component
@MessagingAwareComponent
@RequiredArgsConstructor
public class ObtainGameFinishedOutboxListener {

    private static final Logger log = LoggerFactory.getLogger(ObtainGameFinishedOutboxListener.class);

    private final RabbitTemplate rabbitTemplate;
    private final GameFinishedQueryPort gameFinishedQueryPort;

    @InboxOutboxListener(
        retryCount = 10000,
        batchSize = 100,
        cron = "*/30 * * * * *"
    )
    void obtainBoardWithGameFinished(ObtainGameFinishedOutboxMessage event) {
        UUID boardId = event.gameId();
        log.info("There is unsent finished game with id = {}, sending info with game view to rabbitmq queue", boardId);
        GameFinishedView result = gameFinishedQueryPort.findById(boardId)
            .orElseThrow(() -> new GameNotFoundException(boardId));
        rabbitTemplate.convertAndSend(OBTAIN_GAME_QUEUE, result);
        log.info("Successfully sent info with finished game with id = {} to rabbitmq queue", boardId);
    }
}
