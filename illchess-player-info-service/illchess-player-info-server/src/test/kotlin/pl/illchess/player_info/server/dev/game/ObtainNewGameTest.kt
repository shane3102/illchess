package pl.illchess.player_info.server.dev.game

import io.quarkus.test.junit.QuarkusTest
import io.smallrye.reactive.messaging.annotations.Merge
import java.time.LocalDateTime
import java.util.UUID
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage.PerformedMovesRabbitMqMessage
import pl.illchess.player_info.application.game.query.out.model.GameView


@QuarkusTest
open class ObtainNewGameTest : ObtainNewGameSpecification() {

    @Merge
    @Channel("obtain-game")
    private lateinit var gameSavedEmitter: Emitter<ObtainNewGameRabbitMqMessage>

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
        gameSavedEmitter.send(
            ObtainNewGameRabbitMqMessage(
                gameIdUUID,
                whiteUsernameText,
                blackUsernameText,
                gameResult,
                endTime,
                performedMoves
            )
        )

        // then
        Thread.sleep(500)

        val responseGameView = getGameViewById(gameIdUUID)
            .body()
            .`as`(GameView::class.java)

        assertEquals(whiteUsernameText, responseGameView.whiteUserGameInfo.username)
        assertEquals(blackUsernameText, responseGameView.blackUserGameInfo.username)
        assertEquals(gameResult, responseGameView.gameResult)
        assertEquals(performedMoves.size, responseGameView.performedMoves.size)

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
        gameSavedEmitter.send(
            ObtainNewGameRabbitMqMessage(
                gameIdUUID,
                whiteUsernameText,
                blackUsernameText,
                gameResult,
                endTime,
                performedMoves
            )
        )

        // then
        Thread.sleep(100)

        val responseGameView = getGameViewById(gameIdUUID)
            .body()
            .`as`(GameView::class.java)

        assertEquals(whiteUsernameText, responseGameView.whiteUserGameInfo.username)
        assertEquals(blackUsernameText, responseGameView.blackUserGameInfo.username)
        assertEquals(gameResult, responseGameView.gameResult)
        assertEquals(performedMoves.size, responseGameView.performedMoves.size)
    }
}