package pl.messaging.inbox.annotation

annotation class InboxListener(
    val retryCount: Int = 5,
    val batchSize: Int = 10,
    val cron: String = "",
    val fixedDelay: Int = -1,
    val fixedRate: Int = -1,
    val initialDelay: Int = -1,
)