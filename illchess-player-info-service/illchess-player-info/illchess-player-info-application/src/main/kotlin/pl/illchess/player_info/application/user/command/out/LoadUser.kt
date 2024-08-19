package pl.illchess.player_info.application.user.command.out

import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

interface LoadUser {

    fun load(id: UserId): User?

    fun load(username: Username): User?
}