package pl.messaging.inbox.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import pl.messaging.inbox.annotation.InboxAwareComponent

@Configuration
class InboxConfiguration(
    @Autowired
    @InboxAwareComponent
    val inboxes: List<Any>
) {

    @Bean
    fun scheduledTaskRegistrar(taskScheduler: TaskScheduler): ScheduledTaskRegistrar {
        val scheduledTaskRegistrar = ScheduledTaskRegistrar()
        scheduledTaskRegistrar.setScheduler(taskScheduler)
        return scheduledTaskRegistrar
    }

    @Bean
    fun taskScheduler(): TaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = inboxes.size
        return scheduler
    }

}