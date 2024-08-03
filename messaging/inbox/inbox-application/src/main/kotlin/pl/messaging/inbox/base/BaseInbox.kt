package pl.messaging.inbox.base

import pl.messaging.inbox.model.InboxMessage
import java.util.function.Consumer

class BaseInbox<T : InboxMessage>(
    val retryCount: Int,
    val batchSize: Int,
    val cron: String,
    val type: Class<out T>,
    val performedJob: Consumer<T>
)