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
import pl.illchess.stockfish.application.board.command.out.JoinOrInitializeBoard
import pl.illchess.stockfish.application.board.command.out.LoadBoard
import pl.illchess.stockfish.application.board.command.out.LoadBoardAdditionalInfo
import pl.illchess.stockfish.domain.board.domain.BoardAdditionalInfo
import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.bot.command.PerformMove
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username

@ApplicationScoped
class OkHttpLoadBoardAdapter(
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: ObjectMapper = jacksonObjectMapper().configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        false
    )
) : LoadBoard, LoadBoardAdditionalInfo, JoinOrInitializeBoard, BotPerformMove, BotResignGame, BotQuitNotYetStartedGame {

    @field:ConfigProperty(name = "urls.game-service")
    lateinit var gameServiceUrl: String

    override fun loadBoard(boardId: BoardId): FenBoardPosition? {
        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/board/fen/${boardId.uuid}")
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

    override fun loadBoardAdditionalInfo(boardId: BoardId): BoardAdditionalInfo? {
        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/board/refresh/info/${boardId.uuid}")
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val boardAdditionalInfo: BoardAdditionalInfo? =
            if (response.body == null || response.code != 200) null
            else objectMapper.readValue(
                response.body?.string(),
                BoardAdditionalInfoViewResponse::class.java
            ).let {
                BoardAdditionalInfo(
                    BoardId(it.gameId),
                    it.currentPlayerColor,
                    Username(it.whitePlayer.username),
                    if (it.blackPlayer == null) null else Username(it.blackPlayer.username),
                    it.gameState,
                    it.victoriousPlayerColor
                )
            }

        return boardAdditionalInfo
    }

    override fun joinOrInitialize(username: Username): BoardId? {
        val initializeNewBoardRequest = InitializeNewBoardRequest(username.text)

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(initializeNewBoardRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/board/join-or-initialize")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val initializedBoardId: BoardId? =
            if (response.body == null || response.code != 200) null
            else BoardId(objectMapper.readValue(response.body?.string(), InitializedBoardResponse::class.java).id)

        return initializedBoardId
    }

    override fun performMove(command: PerformMove) {
        val movePieceRequest = toMovePieceRequest(command)

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(movePieceRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/board/move-piece")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        call.execute()
    }

    override fun botResignGame(bot: Bot) {
        val resignGameRequest = ResignGameRequest(bot.currentBoardId!!.uuid, bot.username.text)

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(resignGameRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/board/resign")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        call.execute()
    }

    override fun quitNotYetStartedGame(bot: Bot) {
        val quitNotYetStartedGameRequest = QuitNotYetStartedGameRequest(bot.currentBoardId!!.uuid, bot.username.text)

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(quitNotYetStartedGameRequest).toRequestBody(mediaType)

        val request = Request.Builder()
            .url("${gameServiceUrl}/api/board/quit-not-yet-started")
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
            command.bot.currentBoardId?.uuid!!,
            startSquare,
            endSquare,
            promotePieceType,
            command.bot.username.text
        )
    }

}