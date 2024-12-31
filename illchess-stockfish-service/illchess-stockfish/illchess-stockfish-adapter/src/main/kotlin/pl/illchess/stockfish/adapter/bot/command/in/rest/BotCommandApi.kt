package pl.illchess.stockfish.adapter.bot.command.`in`.rest

import jakarta.ws.rs.DELETE
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestResponse
import pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto.AddBotsRequest
import pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto.DeleteBotsRequest

@Path("/api/bot")
interface BotCommandApi {

    @DELETE
    fun deleteBots(request: DeleteBotsRequest): RestResponse<Void>

    @POST
    fun addBots(request: AddBotsRequest): RestResponse<Void>

}