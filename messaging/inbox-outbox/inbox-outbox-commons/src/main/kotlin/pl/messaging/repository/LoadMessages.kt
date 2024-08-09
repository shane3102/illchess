package pl.messaging.repository

import pl.messaging.model.Message

interface LoadMessages {
    fun loadLatestByTypeNonExpired(className: String, batchSize: Int, maxRetryCount: Int): List<Message>
}