package pl.illchess.player_info.server.dev.player.query

import io.restassured.RestAssured.given
import io.restassured.response.Response
import pl.illchess.player_info.server.dev.Specification

abstract class PlayerViewSpecification : Specification() {

    fun getPlayerRanking(pageNumber: Int, pageSize: Int): Response = given()
        .params(
            mapOf(
                "pageNumber" to pageNumber,
                "pageSize" to pageSize
            )
        )
        .`when`()
        .get("/api/player/ranking")

}