package pl.illchess.player_info.server.dev.player.query

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
import pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto.ObtainNewGameRabbitMqMessage.PerformedMovesRabbitMqMessage
import pl.illchess.player_info.adapter.game.command.out.jpa_streamer.repository.GameJpaStreamerRepository
import pl.illchess.player_info.adapter.player.command.out.jpa_streamer.repository.PlayerJpaStreamerRepository
import pl.illchess.player_info.application.commons.query.model.Page
import pl.illchess.player_info.application.player.query.out.model.PlayerView
import java.time.LocalDateTime
import java.util.UUID

@QuarkusTest
class PlayerViewTest : PlayerViewSpecification() {

    @Inject
    private lateinit var playerJpaStreamerRepository: PlayerJpaStreamerRepository

    @Inject
    private lateinit var gameJpaStreamerRepository: GameJpaStreamerRepository

    @Merge
    @Channel("obtain-game")
    private lateinit var gameSavedEmitter: Emitter<ByteArray>

    @Test
    fun shouldBeLastInRankingWhenLostAllGames() {
        // given
        // TODO hackfix, think of something else
        clearDatabase()

        val whiteUsernameTrackedText = randomString()
        val gameResult = "BLACK_WON"
        val performedMoves = mutableListOf<PerformedMovesRabbitMqMessage>()
        val generatedGames = 7
        val totalPlayers = generatedGames + 1

        // when
        generateSequence {
            ObtainNewGameRabbitMqMessage(
                UUID.randomUUID(),
                whiteUsernameTrackedText,
                randomString(),
                gameResult,
                LocalDateTime.now(),
                performedMoves
            )
        }
            .take(generatedGames)
            .forEach {
                gameSavedEmitter.send(objectMapper.writeValueAsString(it).toByteArray())
            }

        // when
        Thread.sleep(1000)
        val pageSize = 3
        val page = 2

        val playerRanking: Page<PlayerView> = getPlayerRanking(page, pageSize).body()
            .`as`(object : TypeRef<Page<PlayerView>>() {})

        // then
        assertEquals(pageSize, playerRanking.pageSize)
        assertEquals(page, playerRanking.pageNumber)
        assertEquals(totalPlayers - page * pageSize, playerRanking.content.size)
        assertTrue(playerRanking.content.any { it.username == whiteUsernameTrackedText })
    }

    @Test
    fun shouldBeFirstInRankingWhenWonAllGames() {
        // given
        val whiteUsernameTrackedText = randomString()
        val gameResult = "WHITE_WON"
        val performedMoves = mutableListOf<PerformedMovesRabbitMqMessage>()
        val generatedGames = 10

        // when
        generateSequence {
            ObtainNewGameRabbitMqMessage(
                UUID.randomUUID(),
                whiteUsernameTrackedText,
                randomString(),
                gameResult,
                LocalDateTime.now(),
                performedMoves
            )
        }
            .take(generatedGames)
            .forEach {
                gameSavedEmitter.send(objectMapper.writeValueAsString(it).toByteArray())
            }

        // when
        Thread.sleep(1000)
        val pageSize = 3
        val page = 0

        val playerRanking = getPlayerRanking(page, pageSize).body()
            .`as`(object : TypeRef<Page<PlayerView>>() {})

        // then
        assertEquals(pageSize, playerRanking.pageSize)
        assertEquals(page, playerRanking.pageNumber)
        assertEquals(pageSize, playerRanking.content.size)
        assertTrue(playerRanking.content.any { it.username == whiteUsernameTrackedText })
    }


    @Transactional
    fun clearDatabase() {
        gameJpaStreamerRepository.deleteAll()
        playerJpaStreamerRepository.deleteAll()
    }
}