package pl.illchess.player_info.application.player.command.out

import pl.illchess.player_info.domain.player.model.Player
import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.Username

interface LoadPlayer {

    fun load(id: PlayerId): Player?

    fun load(username: Username): Player?
}