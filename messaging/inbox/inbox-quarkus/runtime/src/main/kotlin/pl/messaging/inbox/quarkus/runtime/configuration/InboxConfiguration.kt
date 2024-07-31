package pl.messaging.inbox.quarkus.runtime.configuration

import io.quarkus.arc.DefaultBean
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.Dependent
import jakarta.enterprise.inject.Produces
import pl.messaging.inbox.repository.impl.InMemoryInboxMessageRepository

@Dependent
class InboxConfiguration {

    private val inMemoryInboxMessageRepository = InMemoryInboxMessageRepository()

    @Produces
    @DefaultBean
    fun inMemoryInboxMessageRepository(): InMemoryInboxMessageRepository {
        return inMemoryInboxMessageRepository
    }
}