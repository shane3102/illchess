package pl.illchess.player_info.server.dev.game.query

import io.restassured.RestAssured.given
import io.restassured.response.Response
import pl.illchess.player_info.server.dev.Specification

abstract class GameViewSpecification: Specification() {

    fun getLatestGames(pageNumber: Int, pageSize: Int): Response = given()
        .params(
            mapOf(
                "pageNumber" to pageNumber,
                "pageSize" to pageSize
            )
        )
        .`when`()
        .get("api/game/latest")

}