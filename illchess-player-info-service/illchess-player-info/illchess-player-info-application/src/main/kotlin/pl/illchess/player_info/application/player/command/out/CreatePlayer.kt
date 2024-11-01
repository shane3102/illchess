package pl.illchess.player_info.application.player.command.out

import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.Username

interface CreatePlayer {
    fun create(username: Username): PlayerId
}