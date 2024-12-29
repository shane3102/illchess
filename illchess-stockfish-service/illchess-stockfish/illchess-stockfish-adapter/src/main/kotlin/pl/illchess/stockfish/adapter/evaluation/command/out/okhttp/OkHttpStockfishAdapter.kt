package pl.illchess.stockfish.adapter.evaluation.command.out.okhttp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.quarkus.arc.properties.IfBuildProperty
import jakarta.enterprise.context.ApplicationScoped
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.illchess.stockfish.adapter.evaluation.command.out.okhttp.dto.StockfishApiResponseDto
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves
import pl.illchess.stockfish.domain.evaluation.exception.TopMovesNotAvailableWhenUsingApi
import pl.illchess.stockfish.domain.evaluation.exception.UrlOfStockfishEngineNotProvided

@ApplicationScoped
@IfBuildProperty(name = "working.mode", stringValue = "API")
class OkHttpStockfishAdapter(
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) : LoadBoardEvaluation, LoadBestMoveAndContinuation, LoadTopMoves {

    @field:ConfigProperty(
        name = "urls.stockfish-api",
        defaultValue = "https://stockfish.online/api/s/v2.php"
    )
    lateinit var stockfishApiUrl: String

    @field:ConfigProperty(
        name = "stockfish.default-depth",
        defaultValue = "15"
    )
    lateinit var defaultDepth: String

    override fun loadBoardEvaluation(fenPosition: FenBoardPosition): Evaluation? {
        val response = commonApiCallResponse(fenPosition)
        val result: Evaluation? =
            if (response.body == null) null
            else Evaluation(
                objectMapper.readValue(
                    response.body?.string(),
                    StockfishApiResponseDto::class.java
                )
                    .evaluation
                    .toDouble()
            )

        return result
    }

    override fun loadBestMoveAndContinuation(
        fenPosition: FenBoardPosition,
        depth: Int?
    ): BestMoveAndContinuation? {
        val response = commonApiCallResponse(fenPosition, depth)

        val responseBody: StockfishApiResponseDto? = if (response.body == null) null
        else objectMapper.readValue(
            response.body?.string(),
            StockfishApiResponseDto::class.java
        )

        val result: BestMoveAndContinuation? =
            if (responseBody == null) null
            else BestMoveAndContinuation(
                responseBody.bestmove.split(" ")[1],
                responseBody.continuation.split(" ")
            )

        return result
    }

    private fun commonApiCallResponse(
        fenPosition: FenBoardPosition,
        depth: Int? = null
    ): Response {
        val httpBuilder = (stockfishApiUrl.toHttpUrlOrNull() ?: throw UrlOfStockfishEngineNotProvided())
            .newBuilder()
            .addQueryParameter("fen", fenPosition.value)
            .addQueryParameter("depth", depth?.toString() ?: defaultDepth)
            .build()

        val request: Request = Request.Builder()
            .url(httpBuilder)
            .get()
            .build()

        val call = okHttpClient.newCall(request)
        val response = call.execute()
        return response
    }

    override fun loadTopMoves(
        fenPosition: FenBoardPosition,
        topMoveCount: Int,
        depth: Int?
    ): TopMoves? {
        throw TopMovesNotAvailableWhenUsingApi()
    }
}