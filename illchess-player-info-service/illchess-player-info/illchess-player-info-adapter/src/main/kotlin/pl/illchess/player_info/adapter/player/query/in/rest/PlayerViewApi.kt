package pl.illchess.player_info.adapter.player.query.`in`.rest

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestQuery
import pl.illchess.player_info.application.commons.query.model.Page
import pl.illchess.player_info.application.player.query.out.model.PlayerView

@Path("/api/player")
interface PlayerViewApi {

    @GET
    @Path("/ranking")
    fun ranking(
        @RestQuery pageNumber: Int,
        @RestQuery pageSize: Int
    ): Page<PlayerView>

}