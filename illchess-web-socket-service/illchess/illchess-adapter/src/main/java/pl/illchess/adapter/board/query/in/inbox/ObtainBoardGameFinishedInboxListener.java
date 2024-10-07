package pl.illchess.adapter.board.query.in.inbox;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pl.illchess.adapter.board.query.in.inbox.dto.ObtainBoardGameFinishedInboxMessage;
import pl.illchess.application.board.query.out.BoardGameFinishedQueryPort;
import pl.illchess.application.board.query.out.model.BoardGameFinishedView;
import pl.illchess.domain.board.exception.BoardNotFoundException;
import pl.messaging.annotation.InboxOutboxListener;
import pl.messaging.annotation.MessagingAwareComponent;
import static pl.illchess.adapter.board.query.in.rabbitmq.BoardInfoRabbitMqSupplier.OBTAIN_GAME_QUEUE;

@Component
@MessagingAwareComponent
@RequiredArgsConstructor
public class ObtainBoardGameFinishedInboxListener {

    private static final Logger log = LoggerFactory.getLogger(ObtainBoardGameFinishedInboxListener.class);

    private final RabbitTemplate rabbitTemplate;
    private final BoardGameFinishedQueryPort boardGameFinishedQueryPort;

    @InboxOutboxListener(
        retryCount = 10000,
        batchSize = 100,
        cron = "*/30 * * * * *"
    )
    void obtainBoardWithGameFinished(ObtainBoardGameFinishedInboxMessage event) {
        UUID boardId = event.gameId();
        log.info("There is unsent finished game with id = {}, sending info with game view to rabbitmq queue", boardId);
        BoardGameFinishedView result = boardGameFinishedQueryPort.findById(boardId)
            .orElseThrow(() -> new BoardNotFoundException(boardId));
        rabbitTemplate.convertAndSend(OBTAIN_GAME_QUEUE, result);
        log.info("Successfully sent info with finished game with id = {} to rabbitmq queue", boardId);
    }
}