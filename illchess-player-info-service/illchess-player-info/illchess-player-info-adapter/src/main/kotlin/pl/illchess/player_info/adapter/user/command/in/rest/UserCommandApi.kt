package pl.illchess.player_info.adapter.user.command.`in`.rest

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import pl.illchess.player_info.adapter.user.command.`in`.rest.dto.CreateUserRequest

@Path("/api/player-info/user")
interface UserCommandApi {

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    fun createUser(request: CreateUserRequest): Response
}