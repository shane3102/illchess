package pl.illchess.player_info.domain.user.model

import java.util.UUID
import pl.illchess.player_info.domain.game.model.UserGameInfo
import pl.illchess.player_info.domain.user.command.CreateNewUser

class User(
    val id: UserId,
    val username: Username,
    var currentRanking: UserRankingPoints
) {
    fun applyNewRanking(userGameInfo: UserGameInfo) {
        currentRanking = userGameInfo.rankingPointsAfterGame
    }

    companion object {
        fun createUser(command: CreateNewUser) = User(
            command.id ?: UserId(UUID.randomUUID()),
            command.username,
            UserRankingPoints.defaultRanking()
        )
    }
}
