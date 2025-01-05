package pl.illchess.player_info.adapter.player.command.`in`.rest.dto

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.UUID
import pl.illchess.player_info.application.player.command.`in`.CreateNewPlayerUseCase

@RegisterForReflection
data class CreatePlayerRequest(
    var id: UUID? = null,
    var username: String = ""
) {
    fun toCmd() = CreateNewPlayerUseCase.CreateNewPlayerCmd(id, username)
}
