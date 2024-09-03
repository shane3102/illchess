package pl.illchess.player_info.adapter.game.command.`in`.rabbitmq

import io.smallrye.reactive.messaging.annotations.Blocking
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.messaging.quarkus.runtime.aggregator.InboxOutbox

@ApplicationScoped
class ObtainNewGameRabbitMqListener(
    private val inbox: InboxOutbox,
    private val obtainNewGameUseCase: ObtainNewGameUseCase
) {

    @Blocking
    @Incoming(value = "obtain-game")
    fun obtainNewGameOrSaveItForLaterOnError(message: ObtainNewGameRabbitMqMessage) {
        try {
            obtainNewGameUseCase.obtainNewGame(message.toCmd())
        } catch (e: Exception) {
            inbox.saveMessage(message.toInboxMessage())
        }
    }
}