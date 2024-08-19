package pl.illchess.player_info.domain.user.exception

import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

class UserNotFoundException private constructor(userId: UserId?, username: Username?) : DomainException(
    if (userId != null) "User with id: ${userId.uuid} not found"
    else if (username != null) "User with username: ${username.text} not found"
    else throw DomainException("Illegal usage of user not found exception")
) {
    public constructor(userId: UserId) : this(userId, null)
    public constructor(username: Username) : this(null, username)
}