package pl.messaging.annotation

annotation class InboxOutboxListener(
    val retryCount: Int = 5,
    val batchSize: Int = 10,
    val cron: String = "",
)