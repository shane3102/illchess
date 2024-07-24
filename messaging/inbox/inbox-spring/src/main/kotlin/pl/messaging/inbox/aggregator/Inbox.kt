package pl.messaging.inbox.aggregator

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
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
    @InboxAwareComponent
    private val inboxes: List<Any>
) {

    fun saveMessage(inboxMessage: InboxMessage) {
        saveInboxMessage.saveInboxMessage(inboxMessage)
    }

    private fun loadAndPerformTasks(inboxClass: Class<out Any>, batchSize: Int, consumer: Consumer<InboxMessage>) {
        // TODO what if two inbox aware classes listen for same type of message?
        loadInboxMessages.loadLatestByTypeNonExpired(inboxClass.name, batchSize)
            .forEach { performTask(it, consumer) }
    }

    private fun performTask(inboxMessage: InboxMessage, consumer: Consumer<InboxMessage>) {
        try {
            consumer.accept(inboxMessage)
            // TODO decide if add success flag or delete by property
            deleteInboxMessage.delete(inboxMessage.id)
        } catch (e: Exception) {
            // TODO increment try count
        }
    }

    @PostConstruct
    fun startJobs() {
        val inboxAwareClasses: List<Class<out Any>> = inboxes.stream().map { it::class.java }.toList()
        val inboxList = BaseInboxCreator.extractBaseInboxes(inboxAwareClasses)
        inboxList.forEach { taskRegistrar.scheduleCronTask(CronTask({loadAndPerformTasks(it.type, it.batchSize, it.performedJob)}, it.cron)) }
    }
}
