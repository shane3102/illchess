package pl.messaging.inbox.aggregator

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.config.*
import org.springframework.stereotype.Component
import pl.messaging.inbox.annotation.InboxAwareComponent
import pl.messaging.inbox.base.BaseInbox
import pl.messaging.inbox.base.BaseInboxCreator
import pl.messaging.inbox.exception.NoneOfSchedulingValuesWasProvidedException
import pl.messaging.inbox.exception.OneOfSchedulingConfigShouldBeUsedException
import pl.messaging.inbox.model.InboxMessage
import pl.messaging.inbox.repository.DeleteInboxMessage
import pl.messaging.inbox.repository.LoadInboxMessages
import pl.messaging.inbox.repository.SaveInboxMessage
import java.time.Duration
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

    private fun loadAndPerformTasks(
        inboxClass: Class<out Any>,
        batchSize: Int,
        maxRetryCount: Int,
        consumer: Consumer<InboxMessage>
    ) {
        loadInboxMessages.loadLatestByTypeNonExpired(inboxClass.toString(), batchSize, maxRetryCount)
            .forEach { performTask(it, consumer) }
    }

    private fun performTask(inboxMessage: InboxMessage, consumer: Consumer<InboxMessage>) {
        try {
            consumer.accept(inboxMessage)
            deleteInboxMessage.delete(inboxMessage.id)
        } catch (e: Exception) {
            inboxMessage.incrementCount()
            saveInboxMessage.saveInboxMessage(inboxMessage)
        }
    }

    @PostConstruct
    fun startJobs() {
        val inboxList = BaseInboxCreator.extractBaseInboxes(inboxes)
        inboxList.forEach {
            when (val task = createTaskByInfo(it)) {
                is FixedRateTask -> {
                    taskRegistrar.scheduleFixedRateTask(task)
                }

                is FixedDelayTask -> {
                    taskRegistrar.scheduleFixedDelayTask(task)
                }

                is CronTask -> {
                    taskRegistrar.scheduleCronTask(task)
                }
            }
        }
        taskRegistrar.afterPropertiesSet()
    }

    private fun createTaskByInfo(baseInbox: BaseInbox<InboxMessage>): Task {
        val rateAndDelay = baseInbox.fixedRate != null && baseInbox.fixedDelay != null
        val rateAndCron = baseInbox.fixedRate != null && baseInbox.cron != null
        val cronAndDelay = baseInbox.cron != null && baseInbox.fixedDelay != null
        if (
            rateAndDelay ||
            rateAndCron ||
            cronAndDelay
        ) {
            throw OneOfSchedulingConfigShouldBeUsedException()
        }
        if (baseInbox.fixedRate != null) {
            return FixedRateTask(
                {
                    loadAndPerformTasks(
                        baseInbox.type,
                        baseInbox.batchSize,
                        baseInbox.retryCount,
                        baseInbox.performedJob
                    )
                },
                Duration.ofMillis(baseInbox.fixedRate!!.toLong()),
                Duration.ofMillis(if (baseInbox.initialDelay == null) 0L else baseInbox.initialDelay!!.toLong())
            )
        }
        if (baseInbox.fixedDelay != null) {
            return FixedDelayTask(
                {
                    loadAndPerformTasks(
                        baseInbox.type,
                        baseInbox.batchSize,
                        baseInbox.retryCount,
                        baseInbox.performedJob
                    )
                },
                Duration.ofMillis(baseInbox.fixedDelay!!.toLong()),
                Duration.ofMillis(if (baseInbox.initialDelay == null) 0L else baseInbox.initialDelay!!.toLong())
            )
        }
        if (baseInbox.cron != null) {
            return CronTask(
                {
                    loadAndPerformTasks(
                        baseInbox.type,
                        baseInbox.batchSize,
                        baseInbox.retryCount,
                        baseInbox.performedJob
                    )
                },
                baseInbox.cron!!
            )
        }
        throw NoneOfSchedulingValuesWasProvidedException()
    }
}
