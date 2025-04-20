package pl.illchess.stockfish.adapter.board.command.`in`.rest

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.BestMoveAndContinuationResponse
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.EvaluationResponse
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.TopMovesResponse
import java.util.*

@Path("/api/board")
interface BoardCommandApi {

    @GET
    @Path("/{gameId}/evaluate")
    @Produces(MediaType.APPLICATION_JSON)
    fun evaluateBoard(@RestPath gameId: UUID): EvaluationResponse

    @GET
    @Path("/{gameId}/best-move-and-continuation")
    @Produces(MediaType.APPLICATION_JSON)
    fun establishBestMoveAndContinuation(
        @RestPath gameId: UUID,
        @RestQuery depth: Int?
    ): BestMoveAndContinuationResponse

    @GET
    @Path("/{gameId}/top-moves/{moveCount}")
    @Produces(MediaType.APPLICATION_JSON)
    fun establishTopMoves(
        @RestPath moveCount: Int,
        @RestPath gameId: UUID,
        @RestQuery depth: Int?,
    ): TopMovesResponse

}