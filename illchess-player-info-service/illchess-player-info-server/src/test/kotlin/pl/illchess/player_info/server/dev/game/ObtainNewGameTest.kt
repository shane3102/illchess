package pl.illchess.player_info.server.dev.game

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.smallrye.reactive.messaging.annotations.Merge
import jakarta.inject.Inject
import java.util.UUID
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage.PerformedMovesRabbitMqMessage
import pl.illchess.player_info.application.game.query.out.model.GameView
import pl.illchess.player_info.application.user.command.out.SaveUser
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.UserRankingPoints
import pl.illchess.player_info.domain.user.model.Username
import pl.illchess.player_info.server.it.SpecificationResourceIT


@QuarkusTest
@QuarkusTestResource(
    value = SpecificationResourceIT::class,
    restrictToAnnotatedClass = true
)
open class ObtainNewGameTest {

    @Merge
    @Channel("obtain-game")
    private lateinit var gameSavedEmitter: Emitter<ObtainNewGameRabbitMqMessage>

    // TODO use use-case/api instead of out commands
    @Inject
    private lateinit var saveUser: SaveUser

    @Test
    fun shouldObtainNewGameAndSaveToDatabaseAndSendInfoWithSuccessView() {
        // given
        val gameIdUUID = UUID.randomUUID()
        val whiteUsernameText = "white.username"
        val whiteUserId = UUID.randomUUID()
        val blackUsernameText = "black.username"
        val blackUserId = UUID.randomUUID()
        val winningPieceColor = "WHITE"
        val performedMoves = mutableListOf<PerformedMovesRabbitMqMessage>()

        saveUser.save(User(UserId(whiteUserId), Username(whiteUsernameText), UserRankingPoints(0)))
        saveUser.save(User(UserId(blackUserId), Username(blackUsernameText), UserRankingPoints(0)))

        // when
        gameSavedEmitter.send(
            ObtainNewGameRabbitMqMessage(
                gameIdUUID,
                whiteUsernameText,
                blackUsernameText,
                winningPieceColor,
                performedMoves
            )
        )


        // then
        Thread.sleep(100)


        val responseGameView = given()
            .`when`()
            .get("/api/game/$gameIdUUID")
            .body()
            .`as`(GameView::class.java)

        assertEquals(responseGameView.whiteUserGameInfo.username, whiteUsernameText)
        assertEquals(responseGameView.blackUserGameInfo.username, blackUsernameText)
        assertEquals(responseGameView.winningPieceColor, winningPieceColor)
        assertEquals(responseGameView.performedMoves.size, performedMoves.size)

    }
}