package pl.illchess.websocket.server.config.bean;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.illchess.application.board.command.BoardManager;
import pl.illchess.application.board.command.out.LoadBoard;
import pl.illchess.application.board.command.out.SaveBoard;
import pl.illchess.application.commons.command.out.PublishEvent;

import static pl.illchess.adapter.board.query.in.rabbitmq.BoardInfoRabbitMqSupplier.OBTAIN_GAME_QUEUE;

@Configuration
public class BoardBeanConfiguration {

    @Bean(value = OBTAIN_GAME_QUEUE)
    Queue obtainGameQueue() {
        return new Queue(OBTAIN_GAME_QUEUE, false);
    }

    @Bean
    public BoardManager boardManager(
            LoadBoard loadBoard,
            SaveBoard saveBoard,
            PublishEvent eventPublisher
    ) {
        return new BoardManager(
                loadBoard,
                saveBoard,
                eventPublisher
        );
    }

}
