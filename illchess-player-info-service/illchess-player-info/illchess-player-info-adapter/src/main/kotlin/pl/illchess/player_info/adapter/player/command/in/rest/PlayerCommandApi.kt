package pl.illchess.player_info.adapter.player.command.`in`.rest

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import pl.illchess.player_info.adapter.player.command.`in`.rest.dto.CreatePlayerRequest

@Path("/api/player-info/user")
interface PlayerCommandApi {

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    fun createUser(request: CreatePlayerRequest): Response
}