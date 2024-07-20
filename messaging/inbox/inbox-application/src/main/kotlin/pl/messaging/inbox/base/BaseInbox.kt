package pl.messaging.inbox.base

import pl.messaging.inbox.annotation.configuration.InboxWorkingMode
import pl.messaging.inbox.model.InboxMessage
import java.util.function.Consumer

class BaseInbox<T : InboxMessage>(
    val workingMode: InboxWorkingMode,
    val retryCount: Int,
    val batchSize: Int,
    val cron: String,
    val type: Class<T>,
    val performedJob: Consumer<T>
)