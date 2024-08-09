package pl.messaging.repository

import pl.messaging.model.Message

interface SaveMessage {
    fun saveMessage(message: Message)
}