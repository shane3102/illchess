package pl.illchess.game.server.config.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import pl.shane3102.messaging.aggregator.InboxOutbox;
import pl.shane3102.messaging.configuration.InboxOutboxConfiguration;
import pl.shane3102.messaging.repository.DeleteMessage;
import pl.shane3102.messaging.repository.LoadMessages;
import pl.shane3102.messaging.repository.SaveMessage;

@Configuration
public class InboxOutboxBeanConfiguration extends InboxOutboxConfiguration {

    public InboxOutboxBeanConfiguration(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Bean
    InboxOutbox inboxOutbox(
        ScheduledTaskRegistrar scheduledTaskRegistrar,
        LoadMessages loadMessages,
        SaveMessage saveMessage,
        DeleteMessage deleteMessage,
        ApplicationContext applicationContext
    ) {
        return new InboxOutbox(
            scheduledTaskRegistrar,
            loadMessages,
            saveMessage,
            deleteMessage,
            applicationContext
        );
    }
}
