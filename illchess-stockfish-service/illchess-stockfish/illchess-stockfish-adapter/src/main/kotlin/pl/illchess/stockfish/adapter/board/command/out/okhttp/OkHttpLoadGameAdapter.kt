package pl.illchess.stockfish.adapter.board.command.out.okhttp

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.BoardAdditionalInfoViewResponse
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.FenBoardPositionResponse
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.InitializeNewBoardRequest
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.InitializedBoardResponse
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.PerformMoveRequest
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.QuitNotYetStartedGameRequest
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.ResignGameRequest
import pl.illchess.stockfish.application.board.command.out.BotPerformMove
import pl.illchess.stockfish.application.board.command.out.BotQuitNotYetStartedGame
import pl.illchess.stockfish.application.board.command.out.BotResignGame
import pl.illchess.stockfish.application.board.command.out.JoinOrInitializeGame
import pl.illchess.stockfish.application.board.command.out.LoadGame
import pl.illchess.stockfish.application.board.command.out.LoadGameAdditionalInfo
import pl.illchess.stockfish.domain.board.domain.GameAdditionalInfo
import pl.illchess.stockfish.domain.board.domain.GameId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.bot.command.PerformMove
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username

@ApplicationScoped
class OkHttpLoadGameAdapter(
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: ObjectMapper = jacksonObjectMapper().configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        false
    )
) : LoadGame, LoadGameAdditionalInfo, JoinOrInitializeGame, BotPerformMove, BotResignGame, BotQuitNotYetStartedGame {

    @field:ConfigProperty(name = "urls.game-service")
    lateinit var gameServiceUrl: String

    override fun loadGame(gameId: GameId): FenBoardPosition? {
        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/game/fen/${gameId.uuid}")
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val fenBoardPosition: FenBoardPosition? =
            if (response.body == null || response.code != 200) null
            else FenBoardPosition(
                objectMapper.readValue(
                    response.body?.string(),
                    FenBoardPositionResponse::class.java
                ).value
            )

        return fenBoardPosition
    }

    override fun loadGameAdditionalInfo(gameId: GameId): GameAdditionalInfo? {
        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/game/refresh/info/${gameId.uuid}")
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val gameAdditionalInfo: GameAdditionalInfo? =
            if (response.body == null || response.code != 200) null
            else objectMapper.readValue(
                response.body?.string(),
                BoardAdditionalInfoViewResponse::class.java
            ).let {
                GameAdditionalInfo(
                    GameId(it.gameId),
                    it.currentPlayerColor,
                    Username(it.whitePlayer.username),
                    if (it.blackPlayer == null) null else Username(it.blackPlayer.username),
                    it.gameState,
                    it.victoriousPlayerColor
                )
            }

        return gameAdditionalInfo
    }

    override fun joinOrInitialize(username: Username): GameId? {
        val initializeNewBoardRequest = InitializeNewBoardRequest(username.text)

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(initializeNewBoardRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/game/join-or-initialize")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val initializedGameId: GameId? =
            if (response.body == null || response.code != 200) null
            else GameId(objectMapper.readValue(response.body?.string(), InitializedBoardResponse::class.java).id)

        return initializedGameId
    }

    override fun performMove(command: PerformMove) {
        val movePieceRequest = toMovePieceRequest(command)

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(movePieceRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/game/move-piece")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        call.execute()
    }

    override fun botResignGame(bot: Bot) {
        val resignGameRequest = ResignGameRequest(bot.currentGameId!!.uuid, bot.username.text)

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(resignGameRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/game/resign")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        call.execute()
    }

    override fun quitNotYetStartedGame(bot: Bot) {
        val quitNotYetStartedGameRequest = QuitNotYetStartedGameRequest(bot.currentGameId!!.uuid, bot.username.text)

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(quitNotYetStartedGameRequest).toRequestBody(mediaType)

        val request = Request.Builder()
            .url("${gameServiceUrl}/api/game/quit-not-yet-started")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        call.execute()
    }

    private fun toMovePieceRequest(command: PerformMove): PerformMoveRequest {
        val startSquare = command.move.substring(0, 2).uppercase()
        val endSquare = command.move.substring(2, 4).uppercase()
        val promotePieceType = if (command.move.length == 5) when (command.move.substring(4, 4)) {
            "q" -> "QUEEN"
            "r" -> "ROOK"
            "b" -> "BISHOP"
            "n", "k" -> "KNIGHT"
            else -> "QUEEN"
        } else null
        return PerformMoveRequest(
            command.bot.currentGameId?.uuid!!,
            startSquare,
            endSquare,
            promotePieceType,
            command.bot.username.text
        )
    }

}