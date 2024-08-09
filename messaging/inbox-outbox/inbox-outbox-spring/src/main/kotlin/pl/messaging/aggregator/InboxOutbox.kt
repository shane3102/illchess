package pl.messaging.aggregator

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.config.CronTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.stereotype.Component
import pl.messaging.annotation.MessagingAwareComponent
import pl.messaging.base.BaseInboxOutboxCreator
import pl.messaging.model.Message
import pl.messaging.repository.DeleteMessage
import pl.messaging.repository.LoadMessages
import pl.messaging.repository.SaveMessage
import java.util.function.Consumer

@Component
class InboxOutbox(
    private val taskRegistrar: ScheduledTaskRegistrar,
    private val loadMessages: LoadMessages,
    private val saveMessage: SaveMessage,
    private val deleteMessage: DeleteMessage,
    @Autowired
    applicationContext: ApplicationContext
) {

    private var inboxes: List<Any> = applicationContext.getBeansWithAnnotation(MessagingAwareComponent::class.java)
        .values.toList()

    fun saveMessage(message: Message) {
        saveMessage.saveMessage(message)
    }

    private fun loadAndPerformTasks(
        inboxClass: Class<out Message>,
        batchSize: Int,
        maxRetryCount: Int,
        consumer: Consumer<Message>
    ) {
        loadMessages.loadLatestByTypeNonExpired(inboxClass.toString(), batchSize, maxRetryCount)
            .forEach { performTask(it, consumer) }
    }

    private fun performTask(message: Message, consumer: Consumer<Message>) {
        try {
            consumer.accept(message)
            deleteMessage.delete(message.id)
        } catch (e: Exception) {
            message.incrementCount()
            saveMessage.saveMessage(message)
        }
    }

    @PostConstruct
    fun startJobs() {
        val inboxList = BaseInboxOutboxCreator.extractBaseInboxes(inboxes)
        inboxList.forEach {

            val performedFunction = {
                loadAndPerformTasks(
                    it.type,
                    it.batchSize,
                    it.retryCount,
                    it.performedJob
                )
            }

            val task = CronTask(
                performedFunction,
                it.cron
            )

            taskRegistrar.scheduleCronTask(task)
        }
        taskRegistrar.afterPropertiesSet()
    }

}
