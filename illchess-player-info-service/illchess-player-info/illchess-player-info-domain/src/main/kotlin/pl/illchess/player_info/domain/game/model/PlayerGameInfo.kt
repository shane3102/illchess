package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.player.model.Player
import pl.illchess.player_info.domain.player.model.PlayerRankingPoints

data class PlayerGameInfo(
    val player: Player,
    val rankingPointsBeforeGame: PlayerRankingPoints,
    val rankingPointsAfterGame: PlayerRankingPoints,
    val rankingPointsChange: PlayerRankingPoints
)
