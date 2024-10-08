package pl.illchess.player_info.adapter.game.command.`in`.rabbitmq

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.smallrye.reactive.messaging.annotations.Blocking
import io.smallrye.reactive.messaging.annotations.Merge
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.messaging.quarkus.runtime.aggregator.InboxOutbox

@ApplicationScoped
class ObtainNewGameRabbitMqListener(
    private val inbox: InboxOutbox,
    private val obtainNewGameUseCase: ObtainNewGameUseCase
) {

    @Merge
    @Blocking
    @Incoming(value = "obtain-game")
    fun obtainNewGameOrSaveItForLaterOnError(json: ByteArray) {
        // TODO try to not make it ByteArray but JsonObject
        val objectMapper = jacksonObjectMapper().findAndRegisterModules()
        val message = objectMapper.readValue(json, ObtainNewGameRabbitMqMessage::class.java)
        try {
            obtainNewGameUseCase.obtainNewGame(message.toCmd())
        } catch (e: DomainException) {
            inbox.saveMessage(message.toInboxMessage())
        }
    }
}