package pl.illchess.player_info.application.user.command.out

import pl.illchess.player_info.domain.user.model.UserId

interface DeleteUser {
    fun deleteUser(userId: UserId)
}