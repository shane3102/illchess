package pl.illchess.player_info.adapter.player.command.`in`.rest

import jakarta.ws.rs.core.Response
import pl.illchess.player_info.adapter.player.command.`in`.rest.dto.CreatePlayerRequest
import pl.illchess.player_info.application.player.command.`in`.CreateNewPlayerUseCase

class PlayerCommandController(
    private val createNewPlayerUseCase: CreateNewPlayerUseCase
) : PlayerCommandApi {
    override fun createUser(request: CreatePlayerRequest): Response {
        createNewPlayerUseCase.createNewPlayer(request.toCmd())
        return Response.ok().build()
    }
}