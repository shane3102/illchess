package pl.illchess.player_info.server.dev.inbox_outbox

import io.quarkus.test.junit.mockito.InjectSpy
import jakarta.inject.Inject
import org.mockito.Mockito
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.illchess.player_info.server.dev.Specification
import pl.messaging.repository.LoadMessages

abstract class InboxOutboxRepositoryImplementationSpecification : Specification() {
    @Inject
    protected lateinit var loadMessages: LoadMessages

    @InjectSpy
    protected lateinit var obtainNewGameUseCase: ObtainNewGameUseCase

    protected fun <T> any(type: Class<T>): T = Mockito.any(type)
}