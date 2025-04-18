package pl.illchess.game.adapter.board.query.in.rabbitmq;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.illchess.game.adapter.board.query.in.outbox.dto.ObtainBoardGameFinishedOutboxMessage;
import pl.illchess.game.application.game.query.out.BoardGameFinishedQueryPort;
import pl.illchess.game.application.game.query.out.model.BoardGameFinishedView;
import pl.illchess.game.domain.game.event.GameFinished;
import pl.illchess.game.domain.game.exception.GameNotFoundException;
import pl.illchess.game.domain.commons.exception.DomainException;
import pl.shane3102.messaging.aggregator.InboxOutbox;

@Service
@RequiredArgsConstructor
public class BoardInfoRabbitMqSupplier implements BoardViewRabbitMqSupplier {

    private static final Logger log = LoggerFactory.getLogger(BoardInfoRabbitMqSupplier.class);
    public static final String OBTAIN_GAME_QUEUE = "obtain-game";

    private final InboxOutbox inboxOutbox;
    private final RabbitTemplate rabbitTemplate;
    private final BoardGameFinishedQueryPort boardGameFinishedQueryPort;

    @Override
    public BoardGameFinishedView gameFinishedSupplier(GameFinished gameFinished) {
        try {
            UUID boardId = gameFinished.gameId().uuid();
            log.info("Game at board with id = {} was finished, sending info with game view to rabbitmq queue", boardId);
            BoardGameFinishedView result = boardGameFinishedQueryPort.findById(boardId)
                .orElseThrow(() -> new GameNotFoundException(boardId));
            rabbitTemplate.convertAndSend(OBTAIN_GAME_QUEUE, result);
            log.info("Successfully sent info with finished game with id = {} to rabbitmq queue", boardId);
            return result;
        } catch (DomainException e) {
            inboxOutbox.saveMessage(new ObtainBoardGameFinishedOutboxMessage(gameFinished.gameId().uuid()));
            throw e;
        }
    }
}
