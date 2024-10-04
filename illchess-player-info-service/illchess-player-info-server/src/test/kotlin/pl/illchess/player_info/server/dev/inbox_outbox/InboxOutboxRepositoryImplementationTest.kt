package pl.illchess.player_info.server.dev.inbox_outbox

import io.quarkus.test.junit.QuarkusTest
import io.smallrye.reactive.messaging.annotations.Merge
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doThrow
import org.testcontainers.shaded.org.awaitility.Awaitility
import pl.illchess.player_info.adapter.game.command.`in`.inbox.dto.ObtainNewGameInboxMessage
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.illchess.player_info.domain.commons.exception.DomainException

@QuarkusTest
class InboxOutboxRepositoryImplementationTest : InboxOutboxRepositoryImplementationSpecification() {

    @Merge
    @Channel("obtain-game")
    private lateinit var gameSavedEmitter: Emitter<ObtainNewGameRabbitMqMessage>

    @Test
    fun shouldBeSavedAsInboxOutboxEventOnFailing() {
        // given
        doThrow(DomainException("lol"))
            .`when`(obtainNewGameUseCase)
            .obtainNewGame(any(ObtainNewGameUseCase.ObtainNewGameCmd::class.java))

        val gameIdUUID = UUID.randomUUID()
        val whiteUsernameText = randomString()
        val blackUsernameText = randomString()
        val gameResult = "WHITE_WON"
        val performedMoves = mutableListOf<ObtainNewGameRabbitMqMessage.PerformedMovesRabbitMqMessage>()
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
        Thread.sleep(500)

        // then


        Awaitility.await().pollInterval(Duration.ofSeconds(1)).atMost(5, TimeUnit.SECONDS)
            .untilAsserted {
                val inboxOutboxMessagesOfObtainingNewGame = loadMessages.loadLatestByTypeNonExpired(
                    ObtainNewGameInboxMessage::class.toString(),
                    100,
                    100
                ).map { it as ObtainNewGameInboxMessage }

                assertEquals(1, inboxOutboxMessagesOfObtainingNewGame.size)
                assertEquals(gameIdUUID, inboxOutboxMessagesOfObtainingNewGame[0].gameId)
                assertEquals(gameResult, inboxOutboxMessagesOfObtainingNewGame[0].gameResult)
                assertEquals(performedMoves.size, inboxOutboxMessagesOfObtainingNewGame[0].performedMoves.size)
                assertEquals(endTime, inboxOutboxMessagesOfObtainingNewGame[0].endTime)
                assertEquals(whiteUsernameText, inboxOutboxMessagesOfObtainingNewGame[0].whiteUsername)
                assertEquals(blackUsernameText, inboxOutboxMessagesOfObtainingNewGame[0].blackUsername)

                assertNotNull(inboxOutboxMessagesOfObtainingNewGame[0].id)
                assertNotNull(inboxOutboxMessagesOfObtainingNewGame[0].occurredOn)
                assertNotNull(inboxOutboxMessagesOfObtainingNewGame[0].retryCount)
            }

    }

}