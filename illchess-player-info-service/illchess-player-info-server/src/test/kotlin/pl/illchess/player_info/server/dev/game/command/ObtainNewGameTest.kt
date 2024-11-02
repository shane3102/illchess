package pl.illchess.player_info.server.dev.game.command

import io.quarkus.test.junit.QuarkusTest
import io.smallrye.reactive.messaging.annotations.Merge
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.org.awaitility.Awaitility.await
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage.PerformedMovesRabbitMqMessage
import pl.illchess.player_info.application.game.query.out.model.GameView
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit.SECONDS


@QuarkusTest
open class ObtainNewGameTest : ObtainNewGameSpecification() {

    @Merge
    @Channel("obtain-game")
    private lateinit var gameSavedEmitter: Emitter<ByteArray>

    @Test
    fun shouldObtainNewGameAndSaveToDatabaseAndSendInfoWithSuccessView() {
        // given
        val gameIdUUID = UUID.randomUUID()
        val whiteUsernameText = randomString()
        val whiteUserId = UUID.randomUUID()
        val blackUsernameText = randomString()
        val blackUserId = UUID.randomUUID()
        val gameResult = "WHITE_WON"
        val performedMoves = mutableListOf<PerformedMovesRabbitMqMessage>()
        val endTime = LocalDateTime.now()

        addUser(whiteUserId, whiteUsernameText).then().statusCode(200)
        addUser(blackUserId, blackUsernameText).then().statusCode(200)

        // when
        val message = ObtainNewGameRabbitMqMessage(
            gameIdUUID,
            whiteUsernameText,
            blackUsernameText,
            gameResult,
            endTime,
            performedMoves
        )
        gameSavedEmitter.send(objectMapper.writeValueAsString(message).toByteArray())

        // then

        await().pollInterval(Duration.ofSeconds(1)).atMost(5, SECONDS)
            .untilAsserted {
                val responseGameView = getGameViewById(gameIdUUID)
                    .body()
                    .`as`(GameView::class.java)

                assertEquals(whiteUsernameText, responseGameView.whiteUserGameInfo.username)
                assertEquals(blackUsernameText, responseGameView.blackUserGameInfo.username)
                assertEquals(gameResult, responseGameView.gameResult)
                assertEquals(performedMoves.size, responseGameView.performedMoves.size)
            }

    }

    @Test
    fun shouldObtainNewGameWithNotSavedUsersAndSaveToDatabaseAndSendInfoWithSuccessView() {
        // given
        val gameIdUUID = UUID.randomUUID()
        val whiteUsernameText = randomString()
        val blackUsernameText = randomString()
        val gameResult = "WHITE_WON"
        val performedMoves = mutableListOf<PerformedMovesRabbitMqMessage>()
        val endTime = LocalDateTime.now()

        // when
        val message = ObtainNewGameRabbitMqMessage(
            gameIdUUID,
            whiteUsernameText,
            blackUsernameText,
            gameResult,
            endTime,
            performedMoves
        )
        gameSavedEmitter.send(objectMapper.writeValueAsString(message).toByteArray())

        // then

        await().pollInterval(Duration.ofSeconds(1)).atMost(5, SECONDS)
            .untilAsserted {
                val responseGameView = getGameViewById(gameIdUUID)
                    .body()
                    .`as`(GameView::class.java)

                assertEquals(whiteUsernameText, responseGameView.whiteUserGameInfo.username)
                assertEquals(blackUsernameText, responseGameView.blackUserGameInfo.username)
                assertEquals(gameResult, responseGameView.gameResult)
                assertEquals(performedMoves.size, responseGameView.performedMoves.size)
            }

    }
}