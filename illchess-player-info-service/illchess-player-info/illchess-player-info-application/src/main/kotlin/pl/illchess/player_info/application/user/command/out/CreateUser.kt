package pl.illchess.player_info.application.user.command.out

import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

interface CreateUser {
    fun create(username: Username): UserId
}