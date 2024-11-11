package pl.illchess.stockfish.adapter.board.command.`in`.rest

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestPath
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.BestMoveAndContinuationResponse
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.EvaluationResponse
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.TopMovesResponse
import java.util.*

@Path("/api/board")
interface BoardCommandApi {

    @GET
    @Path("/evaluate/{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun evaluateBoard(@RestPath boardId: UUID): EvaluationResponse

    @GET
    @Path("/best-move-and-continuation/{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun establishBestMoveAndContinuation(@RestPath boardId: UUID): BestMoveAndContinuationResponse

    @GET
    @Path("/top-moves/{moveCount}/{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun establishTopMoves(@RestPath moveCount: Int, @RestPath boardId: UUID): TopMovesResponse

}