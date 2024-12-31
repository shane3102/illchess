package pl.illchess.stockfish.adapter.board.command.out.okhttp

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
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.InitializeNewBoardRequest
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.PerformMoveRequest
import pl.illchess.stockfish.adapter.board.command.out.okhttp.dto.ResignGameRequest
import pl.illchess.stockfish.application.board.command.out.BotPerformMove
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
import java.util.UUID

@ApplicationScoped
class OkHttpLoadBoardAdapter(
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) : LoadBoard, LoadBoardAdditionalInfo, JoinOrInitializeBoard, BotPerformMove, BotResignGame {

    @field:ConfigProperty(name = "urls.game-service")
    lateinit var gameServiceUrl: String

    override fun loadBoard(boardId: BoardId): FenBoardPosition? {
        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/board/fen/${boardId.uuid}")
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val fenBoardPosition: FenBoardPosition? =
            if (response.body == null) null
            else objectMapper.readValue(response.body?.string(), FenBoardPosition::class.java)

        return fenBoardPosition
    }

    override fun loadBoardAdditionalInfo(boardId: BoardId): BoardAdditionalInfo? {
        val request: Request = Request.Builder()
            .url("${gameServiceUrl}/api/board/refresh/info/${boardId.uuid}}")
            .build()
        val call = okHttpClient.newCall(request)
        val result = call.execute()

        val boardAdditionalInfo: BoardAdditionalInfo? =
            if (result.body == null) null
            else objectMapper.readValue(
                result.body?.string(),
                BoardAdditionalInfoViewResponse::class.java
            ).let {
                BoardAdditionalInfo(
                    BoardId(it.boardId),
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
            if (response.body == null) null
            else BoardId(objectMapper.readValue(response.body?.string(), UUID::class.java))

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