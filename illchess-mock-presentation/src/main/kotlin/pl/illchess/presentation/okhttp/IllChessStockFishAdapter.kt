package pl.illchess.presentation.okhttp

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.SocketTimeoutException
import java.util.Properties
import java.util.UUID
import okhttp3.OkHttpClient
import okhttp3.Request
import pl.illchess.presentation.properties.PropertiesLoader

class IllChessStockFishAdapter(
    private val properties: Properties = PropertiesLoader.loadProperties(),
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: ObjectMapper = jacksonObjectMapper().configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        false
    )
) {

    fun loadTopMoves(boardId: UUID, moveCount: Int): TopMoves? {
        try {
            val illChessStockfishUrl = properties.getProperty("illchess.stockfish.url")
            val depth = properties.getProperty("depth")
            val request: Request = Request.Builder()
                .url("$illChessStockfishUrl/api/board/$boardId/top-moves/$moveCount?depth=$depth")
                .build()
            val call = okHttpClient.newCall(request)
            val response = call.execute()

            val bestMoves = response.body?.string()

            return if (response.body == null) null else objectMapper.readValue(
                bestMoves,
                TopMoves::class.java
            )
        } catch (e: SocketTimeoutException) {
            return loadTopMoves(boardId, moveCount)
        }
    }

    data class TopMoves(val topMoves: List<String>)

}