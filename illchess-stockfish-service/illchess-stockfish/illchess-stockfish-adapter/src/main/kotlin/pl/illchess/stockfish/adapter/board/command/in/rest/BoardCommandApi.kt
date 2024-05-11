package pl.illchess.stockfish.adapter.board.command.`in`.rest

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestPath
import pl.illchess.stockfish.adapter.board.command.`in`.rest.dto.EvaluationResponse
import java.util.*

@Path("/api/stockfish/board")
interface BoardCommandApi {

    @GET
    @Path("/evaluate/{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun evaluateBoard(@RestPath boardId: UUID): EvaluationResponse

}