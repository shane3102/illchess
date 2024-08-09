package pl.messaging.quarkus.runtime.aggregator

import io.quarkus.arc.Arc
import io.quarkus.arc.ClientProxy.unwrap
import io.quarkus.arc.InstanceHandle
import io.quarkus.scheduler.Scheduler
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import pl.messaging.base.BaseInboxOutboxCreator
import pl.messaging.model.Message
import pl.messaging.quarkus.runtime.annotation.MessagingAwareComponent
import pl.messaging.repository.DeleteMessage
import pl.messaging.repository.LoadMessages
import pl.messaging.repository.SaveMessage
import java.util.function.Consumer
import kotlin.reflect.full.createInstance


@ApplicationScoped
class InboxOutbox {

    @Inject
    @field:Default
    private lateinit var loadMessages: LoadMessages

    @Inject
    @field:Default
    private lateinit var saveMessage: SaveMessage

    @Inject
    @field:Default
    private lateinit var deleteMessage: DeleteMessage

    @Inject
    @field:Default
    private lateinit var scheduler: Scheduler

    val annotation: Annotation = MessagingAwareComponent::class.createInstance()
    private var inboxes: List<InstanceHandle<Any>> = Arc.container()
        .listAll(Any::class.java, annotation)

    fun saveMessage(inboxMessage: Message) {
        saveMessage.saveMessage(inboxMessage)
    }

    private fun loadAndPerformTasks(
        inboxClass: Class<out Any>,
        batchSize: Int,
        maxRetryCount: Int,
        consumer: Consumer<Message>
    ) {
        loadMessages.loadLatestByTypeNonExpired(inboxClass.toString(), batchSize, maxRetryCount)
            .forEach { performTask(it, consumer) }
    }

    private fun performTask(inboxMessage: Message, consumer: Consumer<Message>) {
        try {
            consumer.accept(inboxMessage)
            deleteMessage.delete(inboxMessage.id)
        } catch (e: Exception) {
            inboxMessage.incrementCount()
            saveMessage.saveMessage(inboxMessage)
        }
    }

    @PostConstruct
    fun startJobs() {
        val inboxList = BaseInboxOutboxCreator.extractBaseInboxes(inboxes.map { it.get() }.map { unwrap(it) })
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
                scheduler.newJob(it.type.name)
                    .setCron(it.cron)
                    .setTask { performedFunction.invoke() }
                    .schedule();
            }

    }
}