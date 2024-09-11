package pl.illchess.player_info.domain.user.command

import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

data class CreateNewUser(
    val id: UserId? = null,
    val username: Username
)