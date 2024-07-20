package pl.messaging.inbox.repository

import java.util.UUID

interface DeleteInboxMessage {
    fun delete(id: UUID)
}