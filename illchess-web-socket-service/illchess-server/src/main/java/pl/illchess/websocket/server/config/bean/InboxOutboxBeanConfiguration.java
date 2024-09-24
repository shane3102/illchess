package pl.illchess.websocket.server.config.bean;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import pl.messaging.aggregator.InboxOutbox;
import pl.messaging.configuration.InboxOutboxConfiguration;
import pl.messaging.repository.DeleteMessage;
import pl.messaging.repository.LoadMessages;
import pl.messaging.repository.SaveMessage;

@Configuration
public class InboxOutboxBeanConfiguration extends InboxOutboxConfiguration {

    public InboxOutboxBeanConfiguration(@NotNull ApplicationContext applicationContext) {
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
