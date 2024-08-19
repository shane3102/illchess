package pl.illchess.player_info.application.user.command.out

import pl.illchess.player_info.domain.user.model.User

interface SaveUser {
    fun save(user: User)
}