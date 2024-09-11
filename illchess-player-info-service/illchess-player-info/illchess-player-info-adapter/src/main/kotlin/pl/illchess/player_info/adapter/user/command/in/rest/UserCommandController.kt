package pl.illchess.player_info.adapter.user.command.`in`.rest

import jakarta.ws.rs.core.Response
import pl.illchess.player_info.adapter.user.command.`in`.rest.dto.CreateUserRequest
import pl.illchess.player_info.application.user.command.`in`.CreateNewUserUseCase

class UserCommandController(
    private val createNewUserUseCase: CreateNewUserUseCase
) : UserCommandApi {
    override fun createUser(request: CreateUserRequest): Response {
        createNewUserUseCase.createNewUser(request.toCmd())
        return Response.ok().build()
    }
}