package pl.illchess.player_info.server.dev.game.query

import io.quarkus.test.junit.QuarkusTest
import io.restassured.common.mapper.TypeRef
import io.smallrye.reactive.messaging.annotations.Merge
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage
import pl.illchess.player_info.adapter.game.command.out.jpa_streamer.repository.GameJpaStreamerRepository
import pl.illchess.player_info.adapter.player.command.out.jpa_streamer.repository.PlayerJpaStreamerRepository
import pl.illchess.player_info.application.commons.query.model.Page
import pl.illchess.player_info.application.game.query.out.model.GameSnippetView
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

@QuarkusTest
class GameViewTest : GameViewSpecification() {

    @Inject
    private lateinit var playerJpaStreamerRepository: PlayerJpaStreamerRepository

    @Inject
    private lateinit var gameJpaStreamerRepository: GameJpaStreamerRepository

    @Merge
    @Channel("obtain-game")
    private lateinit var gameSavedEmitter: Emitter<ByteArray>

    @Test
    fun shouldAddedGamesBeOnTopWhenObtainLatestGames() {
        // given
        // TODO hackfix, think of something else
        clearDatabase()

        val performedMoves = mutableListOf<ObtainNewGameRabbitMqMessage.PerformedMovesRabbitMqMessage>()
        val generatedGames = 7

        // when
        val gamesAdded: MutableList<ObtainNewGameRabbitMqMessage> = mutableListOf()
        for (i in 0..<generatedGames) {
            gamesAdded.add(
                ObtainNewGameRabbitMqMessage(
                    UUID.randomUUID(),
                    randomString(),
                    randomString(),
                    if (Random.Default.nextFloat() > 0.5) "BLACK_WON" else "WHITE_WON",
                    "CHECKMATE",
                    LocalDateTime.now().plusDays(i.toLong()),
                    performedMoves
                )
            )

        }

        val expectedGames: MutableList<ObtainNewGameRabbitMqMessage> = mutableListOf()

        gamesAdded.reverse()
        gamesAdded.forEach {
            gameSavedEmitter.send(objectMapper.writeValueAsString(it).toByteArray())
            expectedGames.add(it)
        }

        // when
        Thread.sleep(2000)
        val result = getLatestGames(0, generatedGames).body()
            .`as`(object : TypeRef<Page<GameSnippetView>>() {})

        // then
        println(expectedGames.map { it.endTime })
        println(result.content.map { it.endTime })

        assertEquals(expectedGames.size, result.content.size)
        for (i in 0..<result.content.size) {
            assertEquals(expectedGames[i].gameId, result.content[i].id)
            assertEquals(expectedGames[i].blackUsername, result.content[i].blackUsername)
            assertEquals(expectedGames[i].whiteUsername, result.content[i].whiteUsername)
            if (i < result.content.size - 1) {
                assertTrue(result.content[i].endTime.isAfter(result.content[i + 1].endTime))
            }
        }
    }

    @Transactional
    fun clearDatabase() {
        gameJpaStreamerRepository.deleteAll()
        playerJpaStreamerRepository.deleteAll()
    }

}