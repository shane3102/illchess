package pl.illchess.presentation.okhttp

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import pl.illchess.presentation.properties.PropertiesLoader
import java.util.*

class IllChessStockFishAdapter(
    private val properties: Properties = PropertiesLoader.loadProperties(),
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
) {

    fun loadTopMoves(boardId: UUID, moveCount: Int): TopMoves? {
        val illChessStockfishUrl = properties.getProperty("illchess.stockfish.url")
        val request: Request = Request.Builder()
            .url("$illChessStockfishUrl/api/board/top-moves/$moveCount/$boardId")
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val bestMoves = response.body?.string()

        val fenBoardPosition: TopMoves? =
            if (response.body == null) null
            else objectMapper.readValue(bestMoves, TopMoves::class.java)

        return fenBoardPosition
    }

    data class TopMoves(val topMoves: List<String>)

}