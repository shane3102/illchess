package pl.illchess.player_info.server.dev.game

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import java.util.UUID
import pl.illchess.player_info.adapter.player.command.`in`.rest.dto.CreatePlayerRequest
import pl.illchess.player_info.server.dev.Specification

abstract class ObtainNewGameSpecification : Specification() {

    fun addUser(id: UUID, username: String): Response = given()
        .contentType(ContentType.JSON)
        .body(CreatePlayerRequest(id, username))
        .`when`()
        .post("/api/player-info/user/create")

    fun getGameViewById(gameIdUUID: UUID): Response = given()
        .`when`()
        .get("/api/game/$gameIdUUID")
}