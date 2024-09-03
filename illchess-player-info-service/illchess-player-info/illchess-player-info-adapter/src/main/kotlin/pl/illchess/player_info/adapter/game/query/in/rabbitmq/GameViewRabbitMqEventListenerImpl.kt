package pl.illchess.player_info.adapter.game.query.`in`.rabbitmq

import io.quarkus.vertx.ConsumeEvent
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.player_info.application.game.query.out.GameViewQueryPort
import pl.illchess.player_info.application.game.query.out.model.GameView
import pl.illchess.player_info.domain.game.event.ErrorWhileSavingGameEvent
import pl.illchess.player_info.domain.game.event.GameSavedEvent
import pl.illchess.player_info.domain.game.exception.GameNotFoundException
import java.util.UUID

@ApplicationScoped
class GameViewRabbitMqEventListenerImpl(
    private val gameViewQueryPort: GameViewQueryPort,
    @Channel("obtain-game-success") private val gameSavedEmitter: Emitter<GameView>,
    @Channel("obtain-game-failure") private val gameSavingErrorEmitter: Emitter<UUID>
) : GameViewRabbitMqEventListener {

    @ConsumeEvent("game.saved")
    override fun sendGameViewOnGameSaved(gameSavedEvent: GameSavedEvent): GameView {
        log.info("Received game saved event of game with id: ${gameSavedEvent.gameId.uuid}, sending info with game view")
        val result = (gameViewQueryPort.findById(gameSavedEvent.gameId.uuid)
            ?: throw GameNotFoundException(gameSavedEvent.gameId))
        gameSavedEmitter.send(result)
        log.info("Successfully sent info with game view of game with id: ${gameSavedEvent.gameId.uuid}. Result: $result")
        return result
    }

    @ConsumeEvent("game.error")
    override fun sendErrorInfoOnErrorWhileSavingGame(errorWhileSavingGameEvent: ErrorWhileSavingGameEvent): UUID {
        val result = errorWhileSavingGameEvent.gameId.uuid
        log.error("Received error while saving game event. Sending info with id: $result of failing game which was saved")
        gameSavingErrorEmitter.send(result)
        log.error("Successfully sent info with id: $result of failing game which was saved")
        return result
    }
}

private val log: Logger = LoggerFactory.getLogger(GameViewRabbitMqEventListenerImpl::class.java)