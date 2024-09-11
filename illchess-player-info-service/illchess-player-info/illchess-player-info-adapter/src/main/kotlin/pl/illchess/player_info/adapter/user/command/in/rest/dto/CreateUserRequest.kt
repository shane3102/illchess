package pl.illchess.player_info.adapter.user.command.`in`.rest.dto

import java.util.UUID
import pl.illchess.player_info.application.user.command.`in`.CreateNewUserUseCase

data class CreateUserRequest(
    var id: UUID? = null,
    var username: String = ""
) {
    fun toCmd() = CreateNewUserUseCase.CreateNewUserCmd(id, username)
}
