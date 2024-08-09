package pl.messaging.repository

import java.util.UUID

interface DeleteMessage {
    fun delete(id: UUID)
}