package pl.messaging.base

import pl.messaging.model.Message
import java.util.function.Consumer

class BaseInboxOutbox<T : Message>(
    val retryCount: Int,
    val batchSize: Int,
    val cron: String,
    val type: Class<out T>,
    val performedJob: Consumer<T>
)