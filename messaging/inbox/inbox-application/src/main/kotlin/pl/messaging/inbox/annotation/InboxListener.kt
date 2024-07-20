package pl.messaging.inbox.annotation

import pl.messaging.inbox.annotation.configuration.InboxWorkingMode
import pl.messaging.inbox.model.InboxMessage
import kotlin.reflect.KClass

annotation class InboxListener(
    val workingMode: InboxWorkingMode,
    val type: KClass<InboxMessage>,
    val retryCount: Int = 5,
    val batchSize: Int = 10,
    val cron: String = "0 12 * * ?"
)