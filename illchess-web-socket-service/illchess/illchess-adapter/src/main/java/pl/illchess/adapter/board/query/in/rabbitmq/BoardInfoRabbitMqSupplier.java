package pl.illchess.adapter.board.query.in.rabbitmq;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.adapter.board.query.in.inbox.dto.ObtainBoardGameFinishedInboxMessage;
import pl.illchess.application.board.query.out.BoardGameFinishedQueryPort;
import pl.illchess.application.board.query.out.model.BoardGameFinishedView;
import pl.illchess.domain.board.event.GameFinished;
import pl.illchess.domain.board.exception.BoardNotFoundException;
import pl.illchess.domain.commons.exception.DomainException;
import pl.messaging.aggregator.InboxOutbox;

@Service
@RequiredArgsConstructor
public class BoardInfoRabbitMqSupplier implements BoardViewRabbitMqSupplier {

    private static final Logger log = LoggerFactory.getLogger(BoardInfoRabbitMqSupplier.class);
    public static final String OBTAIN_GAME_QUEUE = "obtain-game";

    private final InboxOutbox inboxOutbox;
    private final RabbitTemplate rabbitTemplate;
    private final BoardGameFinishedQueryPort boardGameFinishedQueryPort;

    // TODO make connection to rabbitmq on startup
    @Override
    public BoardGameFinishedView gameFinishedSupplier(GameFinished gameFinished) {
        try {
            UUID boardId = gameFinished.boardId().uuid();
            log.info("Game at board with id = {} was finished, sending info with game view to rabbitmq queue", boardId);
            BoardGameFinishedView result = boardGameFinishedQueryPort.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
            rabbitTemplate.convertAndSend(OBTAIN_GAME_QUEUE, result);
            log.info("Successfully sent info with finished game with id = {} to rabbitmq queue", boardId);
            return result;
        } catch (DomainException e) {
            inboxOutbox.saveMessage(new ObtainBoardGameFinishedInboxMessage(gameFinished.boardId().uuid()));
            throw e;
        }
    }
}
