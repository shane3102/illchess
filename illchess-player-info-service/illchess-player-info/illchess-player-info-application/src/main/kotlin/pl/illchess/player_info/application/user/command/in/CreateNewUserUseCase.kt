package pl.illchess.player_info.application.user.command.`in`

import java.util.UUID
import pl.illchess.player_info.domain.user.command.CreateNewUser
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

interface CreateNewUserUseCase {

    fun createNewUser(cmd: CreateNewUserCmd)

    data class CreateNewUserCmd(
        val id: UUID?,
        val username: String
    ) {
        fun toCommand() = CreateNewUser(
            if (id == null) null else UserId(id),
            Username(username)
        )
    }
}