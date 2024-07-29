package pl.messaging.inbox.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import pl.messaging.inbox.annotation.InboxAwareComponent
import pl.messaging.inbox.repository.DeleteInboxMessage
import pl.messaging.inbox.repository.LoadInboxMessages
import pl.messaging.inbox.repository.SaveInboxMessage
import pl.messaging.inbox.repository.impl.InMemoryInboxMessageRepository

@Configuration
open class InboxConfiguration(
    @Autowired
    applicationContext: ApplicationContext
) {

    private var inboxes: List<Any> = applicationContext.getBeansWithAnnotation(InboxAwareComponent::class.java)
        .values.toList()

    val inMemoryInboxMessageRepository = InMemoryInboxMessageRepository()

    @Bean
    open fun scheduledTaskRegistrar(taskScheduler: TaskScheduler): ScheduledTaskRegistrar {
        val scheduledTaskRegistrar = ScheduledTaskRegistrar()
        scheduledTaskRegistrar.setScheduler(taskScheduler)
        scheduledTaskRegistrar.afterPropertiesSet()
        return scheduledTaskRegistrar
    }

    @Bean
    open fun taskScheduler(): TaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = if (inboxes.isEmpty()) 1 else inboxes.size
        return scheduler
    }

    @Bean
    @ConditionalOnMissingBean(LoadInboxMessages::class, SaveInboxMessage::class, DeleteInboxMessage::class)
    open fun inMemoryInboxMessageRepository(): InMemoryInboxMessageRepository {
        return inMemoryInboxMessageRepository
    }

}