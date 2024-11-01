package pl.illchess.player_info.domain.player.command

import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.Username

data class CreateNewPlayer(
    val id: PlayerId? = null,
    val username: Username
)