package pl.illchess.player_info.domain.game.model

import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserRankingPoints

data class UserGameInfo(
    val user: User,
    val rankingPointsBeforeGame: UserRankingPoints,
    val rankingPointsAfterGame: UserRankingPoints,
    val rankingPointsChange: UserRankingPoints
)
