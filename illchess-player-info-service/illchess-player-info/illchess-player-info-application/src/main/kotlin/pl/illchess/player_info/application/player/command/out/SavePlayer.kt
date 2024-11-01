package pl.illchess.player_info.application.player.command.out

import pl.illchess.player_info.domain.player.model.Player

interface SavePlayer {
    fun save(player: Player)
}