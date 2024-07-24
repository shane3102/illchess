package pl.messaging.inbox.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import pl.messaging.inbox.annotation.InboxAwareComponent
import pl.messaging.inbox.repository.DeleteInboxMessage
import pl.messaging.inbox.repository.LoadInboxMessages
import pl.messaging.inbox.repository.SaveInboxMessage
import pl.messaging.inbox.repository.impl.InMemoryInboxMessageRepository

@Configuration
@EnableScheduling
open class InboxConfiguration(
    @Autowired
    @InboxAwareComponent
    val inboxes: List<Any>
) {

    val inMemoryInboxMessageRepository = InMemoryInboxMessageRepository()

    @Bean
    open fun scheduledTaskRegistrar(taskScheduler: TaskScheduler): ScheduledTaskRegistrar {
        val scheduledTaskRegistrar = ScheduledTaskRegistrar()
        scheduledTaskRegistrar.setScheduler(taskScheduler)
        return scheduledTaskRegistrar
    }

    @Bean
    open fun taskScheduler(): TaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = if (inboxes.isEmpty()) 1 else 0
        return scheduler
    }

    @Bean
    @ConditionalOnMissingBean(LoadInboxMessages::class, SaveInboxMessage::class)
    open fun loadInboxMessages(): LoadInboxMessages {
        return inMemoryInboxMessageRepository
    }

    // TODO DeleteInboxMessage zarejestrować tylko w przypadku gdy wiadomości są usuwane
    @Bean
    @ConditionalOnMissingBean(LoadInboxMessages::class, SaveInboxMessage::class, DeleteInboxMessage::class)
    open fun saveInboxMessages(): SaveInboxMessage {
        return inMemoryInboxMessageRepository
    }

}