package pl.illchess.presentation.okhttp

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import pl.illchess.presentation.exception.BoardNotFound
import pl.illchess.presentation.properties.PropertiesLoader
import java.util.*

class IllChessGameAdapter(
    private val properties: Properties = PropertiesLoader.loadProperties(),
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: ObjectMapper = jacksonObjectMapper().configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        false
    )
) {

    fun performMove(movePieceRequest: MovePieceRequest) {
        val illChessGameUrl = properties.getProperty("illchess.game.url")

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(movePieceRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("$illChessGameUrl/api/board/move-piece")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        call.execute()
    }

    fun loadBoardAdditionalInfoView(boardId: UUID): BoardAdditionalInfoView? {
        val illChessGameUrl = properties.getProperty("illchess.game.url")

        val request: Request = Request.Builder()
            .url("$illChessGameUrl/api/board/refresh/info/$boardId")
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        if (response.code == 200) {
            val boardAdditionalInfoView: BoardAdditionalInfoView? =
                if (response.body == null) null
                else objectMapper.readValue(response.body?.string(), BoardAdditionalInfoView::class.java)
            return boardAdditionalInfoView
        } else {
            response.body?.close()
            throw BoardNotFound(boardId)
        }

    }

    fun joinOrInitialize(initializeNewBoardRequest: InitializeNewBoardRequest): InitializedBoardResponse? {
        val illChessGameUrl = properties.getProperty("illchess.game.url")

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(initializeNewBoardRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("$illChessGameUrl/api/board/join-or-initialize")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val initializedBoardResponse: InitializedBoardResponse? =
            if (response.body == null) null
            else objectMapper.readValue(response.body?.string(), InitializedBoardResponse::class.java)

        return initializedBoardResponse
    }

    fun resign(resignGameRequest: ResignGameRequest) {
        val illChessGameUrl = properties.getProperty("illchess.game.url")

        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = objectMapper.writeValueAsString(resignGameRequest).toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("$illChessGameUrl/api/board/resign")
            .put(requestBody)
            .build()
        val call = okHttpClient.newCall(request)
        call.execute()
    }

    data class ResignGameRequest(
        val boardId: UUID,
        val username: String
    )

    data class MovePieceRequest(
        val boardId: UUID,
        val startSquare: String,
        val targetSquare: String,
        val pawnPromotedToPieceType: String?,
        val username: String
    )

    data class BoardAdditionalInfoView(
        val boardId: UUID,
        val currentPlayerColor: String,
        val whitePlayer: PlayerView,
        val blackPlayer: PlayerView?,
        val gameState: String,
        val victoriousPlayerColor: String?
    ) {
        data class PlayerView(
            val username: String
        )
    }

    data class InitializeNewBoardRequest(val username: String)

    data class InitializedBoardResponse(val id: UUID)

}