package pl.illchess.stockfish.adapter.bot.query.`in`.rest

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import pl.illchess.stockfish.application.bot.query.out.model.BotView

@Path("api/bot")
interface BotViewApi {

    @GET
    @Path("/list")
    fun listAllCurrentlyRunBots(): List<BotView>

    @GET
    @Path("/max")
    fun getMaxBotCount(): Int

    @GET
    @Path("/expiration-minutes")
    fun getBotExpirationMinutes(): Long

}