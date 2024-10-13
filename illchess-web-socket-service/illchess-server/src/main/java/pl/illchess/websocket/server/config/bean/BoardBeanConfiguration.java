package pl.illchess.websocket.server.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.illchess.application.board.command.BoardManager;
import pl.illchess.application.board.command.out.DeleteBoard;
import pl.illchess.application.board.command.out.LoadBoard;
import pl.illchess.application.board.command.out.SaveBoard;
import pl.illchess.application.commons.command.out.PublishEvent;

@Configuration
public class BoardBeanConfiguration {

    @Bean
    public BoardManager boardManager(
        LoadBoard loadBoard,
        SaveBoard saveBoard,
        DeleteBoard deleteBoard,
        PublishEvent eventPublisher
    ) {
        return new BoardManager(
            loadBoard,
            saveBoard,
            deleteBoard,
            eventPublisher
        );
    }

}
