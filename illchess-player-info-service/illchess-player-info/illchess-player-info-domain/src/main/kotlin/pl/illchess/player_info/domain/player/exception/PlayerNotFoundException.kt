package pl.illchess.player_info.domain.player.exception

import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.Username

class PlayerNotFoundException private constructor(playerId: PlayerId?, username: Username?) : DomainException(
    if (playerId != null) "Player with id: ${playerId.uuid} not found"
    else if (username != null) "Player with username: ${username.text} not found"
    else throw DomainException("Illegal usage of user not found exception")
) {
    public constructor(playerId: PlayerId) : this(playerId, null)
    public constructor(username: Username) : this(null, username)
}