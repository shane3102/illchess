package pl.illchess.player_info.application.player.query.out.model

import java.util.UUID

data class PlayerView(
    val id: UUID,
    val username: String,
    var currentRanking: Int
)
