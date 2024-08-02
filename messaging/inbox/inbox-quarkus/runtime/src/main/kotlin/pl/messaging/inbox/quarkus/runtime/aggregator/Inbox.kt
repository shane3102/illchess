package pl.messaging.inbox.quarkus.runtime.aggregator

import io.quarkus.arc.Arc
import io.quarkus.arc.ClientProxy.unwrap
import io.quarkus.arc.InstanceHandle
import io.quarkus.scheduler.Scheduler
import io.quarkus.scheduler.Scheduler.JobDefinition
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import pl.messaging.inbox.base.BaseInboxCreator
import pl.messaging.inbox.exception.NoneOfSchedulingValuesWasProvidedException
import pl.messaging.inbox.exception.OneOfSchedulingConfigShouldBeUsedException
import pl.messaging.inbox.model.InboxMessage
import pl.messaging.inbox.quarkus.runtime.annotation.InboxAwareComponent
import pl.messaging.inbox.repository.DeleteInboxMessage
import pl.messaging.inbox.repository.LoadInboxMessages
import pl.messaging.inbox.repository.SaveInboxMessage
import java.util.function.Consumer
import kotlin.reflect.full.createInstance


@ApplicationScoped
class Inbox {

    @Inject
    @field:Default
    private lateinit var loadInboxMessages: LoadInboxMessages

    @Inject
    @field:Default
    private lateinit var saveInboxMessage: SaveInboxMessage

    @Inject
    @field:Default
    private lateinit var deleteInboxMessage: DeleteInboxMessage

    @Inject
    @field:Default
    private lateinit var scheduler: Scheduler

    val annotation: Annotation = InboxAwareComponent::class.createInstance()
    private var inboxes: List<InstanceHandle<Any>> = Arc.container()
        .listAll(Any::class.java, annotation)

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
        val inboxList = BaseInboxCreator.extractBaseInboxes(inboxes.map { it.get() }.map { unwrap(it) })
        inboxList
            .forEach {

                val performedFunction = {
                    loadAndPerformTasks(
                        it.type,
                        it.batchSize,
                        it.retryCount,
                        it.performedJob
                    )
                }
                val jobDefinition: JobDefinition = scheduler.newJob(it.type.name)

                val rateAndDelay = it.fixedRate != null && it.fixedDelay != null
                val rateAndCron = it.fixedRate != null && it.cron != null
                val cronAndDelay = it.cron != null && it.fixedDelay != null
                if (
                    rateAndDelay ||
                    rateAndCron ||
                    cronAndDelay
                ) {
                    throw OneOfSchedulingConfigShouldBeUsedException()
                }

                if (it.fixedDelay != null) {
                    jobDefinition.setInterval("PT%sS".format(it.fixedDelay))
                } else if (it.fixedRate != null) {
                    jobDefinition.setInterval("PT%sS".format(it.fixedRate))
                } else if (it.cron != null) {
                    jobDefinition.setCron(it.cron)
                } else {
                    throw NoneOfSchedulingValuesWasProvidedException()
                }

                jobDefinition.setTask { performedFunction.invoke() }
                jobDefinition.schedule()

            }

    }
}