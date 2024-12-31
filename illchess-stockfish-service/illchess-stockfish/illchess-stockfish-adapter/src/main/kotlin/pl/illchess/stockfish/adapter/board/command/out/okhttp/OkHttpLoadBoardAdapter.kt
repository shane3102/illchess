package pl.illchess.stockfish.adapter.board.command.out.okhttp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import okhttp3.OkHttpClient
import okhttp3.Request
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.illchess.stockfish.application.board.command.out.LoadBoard
import pl.illchess.stockfish.domain.board.domain.BoardId
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition

@ApplicationScoped
class OkHttpLoadBoardAdapter(
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) : LoadBoard {

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
}