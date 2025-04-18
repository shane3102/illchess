package pl.illchess.game.server.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.illchess.game.application.game.command.GameManager;
import pl.illchess.game.application.game.command.out.DeleteGame;
import pl.illchess.game.application.game.command.out.LoadGame;
import pl.illchess.game.application.game.command.out.SaveGame;
import pl.illchess.game.application.commons.command.out.PublishEvent;

@Configuration
public class BoardBeanConfiguration {

    @Bean
    public GameManager boardManager(
        LoadGame loadGame,
        SaveGame saveGame,
        DeleteGame deleteGame,
        PublishEvent eventPublisher
    ) {
        return new GameManager(
            loadGame,
            saveGame,
            deleteGame,
            eventPublisher
        );
    }

}
