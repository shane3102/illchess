package pl.illchess.player_info.adapter.game.query.`in`.rest

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestQuery
import pl.illchess.player_info.application.commons.query.model.Page
import pl.illchess.player_info.application.game.query.out.model.GameSnippetView
import pl.illchess.player_info.application.game.query.out.model.GameView
import java.util.UUID

@Path("/api/game")
interface GameViewApi {

    @GET
    @Path("/{gameId}")
    fun getGameById(gameId: UUID): GameView

    @GET
    @Path("/latest")
    fun getLatestGames(
        @RestQuery pageNumber: Int,
        @RestQuery pageSize: Int
    ): Page<GameSnippetView>
}