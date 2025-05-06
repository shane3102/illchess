package pl.illchess.stockfish.adapter.bot.command.`in`.rest

import jakarta.validation.Valid
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestResponse
import pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto.AddBotRequest
import pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto.DeleteBotRequest

@Path("/api/bot")
interface BotCommandApi {

    @DELETE
    fun deleteBots(request: DeleteBotRequest): RestResponse<Void>

    @POST
    fun addBots(@Valid request: AddBotRequest): RestResponse<Void>

}