package pl.illchess.player_info.application.player.command.out

import pl.illchess.player_info.domain.player.model.PlayerId

interface DeletePlayer {
    fun deleteUser(playerId: PlayerId)
}