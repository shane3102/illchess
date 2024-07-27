package pl.messaging.inbox.base

import pl.messaging.inbox.model.InboxMessage
import java.util.function.Consumer

class BaseInbox<T : InboxMessage>(
    val retryCount: Int,
    val batchSize: Int,
    val cron: String?,
    val fixedDelay: Int?,
    val fixedRate: Int?,
    val initialDelay: Int?,
    val type: Class<T>,
    val performedJob: Consumer<T>
)