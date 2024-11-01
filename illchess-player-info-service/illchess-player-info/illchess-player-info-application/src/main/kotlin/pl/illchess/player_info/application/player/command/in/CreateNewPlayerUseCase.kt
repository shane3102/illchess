package pl.illchess.player_info.application.player.command.`in`

import java.util.UUID
import pl.illchess.player_info.domain.player.command.CreateNewPlayer
import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.Username

interface CreateNewPlayerUseCase {

    fun createNewPlayer(cmd: CreateNewPlayerCmd)

    data class CreateNewPlayerCmd(
        val id: UUID?,
        val username: String
    ) {
        fun toCommand() = CreateNewPlayer(
            if (id == null) null else PlayerId(id),
            Username(username)
        )
    }
}