package pl.illchess.player_info.adapter.game.query.`in`.rest

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import pl.illchess.player_info.application.game.query.out.model.GameView
import java.util.UUID

@Path("/api/player-info/game")
interface GameViewApi {

    @GET
    @Path("/{gameId}")
    fun getGameById(gameId: UUID): GameView
}