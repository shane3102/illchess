package pl.illchess.player_info.server.dev.player

import io.restassured.RestAssured.given
import pl.illchess.player_info.server.dev.Specification

abstract class PlayerViewSpecification : Specification() {

    fun getPlayerRanking(page: Int, pageSize: Int) = given()
        .params(
            mapOf(
                "page" to page,
                "pageSize" to pageSize
            )
        )
        .`when`()
        .get("/api/player/ranking")

}