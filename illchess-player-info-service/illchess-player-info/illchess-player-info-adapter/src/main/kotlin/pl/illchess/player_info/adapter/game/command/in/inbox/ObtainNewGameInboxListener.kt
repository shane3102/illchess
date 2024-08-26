package pl.illchess.player_info.adapter.game.command.`in`.inbox

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.game.command.`in`.inbox.dto.ObtainNewGameInboxMessage
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.messaging.annotation.InboxOutboxListener
import pl.messaging.quarkus.runtime.annotation.MessagingAwareComponent

@ApplicationScoped
@MessagingAwareComponent
class ObtainNewGameInboxListener(
    private val obtainNewGameUseCase: ObtainNewGameUseCase
) {

    @InboxOutboxListener(
        retryCount = 100,
        batchSize = 20,
        cron = "*/5 * * * * ?"
    )
    fun obtainNewGame(inboxMessage: ObtainNewGameInboxMessage) {
        obtainNewGameUseCase.obtainNewGame(inboxMessage.toCmd())
    }

}