package pl.messaging.inbox.aggregator

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.config.CronTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.stereotype.Component
import pl.messaging.inbox.annotation.InboxAwareComponent
import pl.messaging.inbox.base.BaseInboxCreator
import pl.messaging.inbox.model.InboxMessage
import pl.messaging.inbox.repository.DeleteInboxMessage
import pl.messaging.inbox.repository.LoadInboxMessages
import pl.messaging.inbox.repository.SaveInboxMessage
import java.util.function.Consumer

@Component
class Inbox(
    private val taskRegistrar: ScheduledTaskRegistrar,
    private val loadInboxMessages: LoadInboxMessages,
    private val saveInboxMessage: SaveInboxMessage,
    private val deleteInboxMessage: DeleteInboxMessage,
    @Autowired
    applicationContext: ApplicationContext
) {

    private var inboxes: List<Any> = applicationContext.getBeansWithAnnotation(InboxAwareComponent::class.java)
        .values.toList()

    fun saveMessage(inboxMessage: InboxMessage) {
        saveInboxMessage.saveInboxMessage(inboxMessage)
    }

    private fun loadAndPerformTasks(inboxClass: Class<out Any>, batchSize: Int, consumer: Consumer<InboxMessage>) {
        // TODO what if two inbox aware classes listen for same type of message?
        loadInboxMessages.loadLatestByTypeNonExpired(inboxClass.toString(), batchSize)
            .forEach { performTask(it, consumer) }
    }

    private fun performTask(inboxMessage: InboxMessage, consumer: Consumer<InboxMessage>) {
        try {
            consumer.accept(inboxMessage)
            // TODO decide if add success flag or delete by property
            deleteInboxMessage.delete(inboxMessage.id)
        } catch (e: Exception) {
            println(e)
            // TODO increment try count
        }
    }

    @PostConstruct
    fun startJobs() {
        val inboxList = BaseInboxCreator.extractBaseInboxes(inboxes)
        inboxList.forEach {
            taskRegistrar.scheduleCronTask(CronTask({
                loadAndPerformTasks(
                    it.type,
                    it.batchSize,
                    it.performedJob
                )
            }, it.cron))
        }
        taskRegistrar.afterPropertiesSet()
    }
}
