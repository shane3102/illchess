package pl.illchess.player_info.domain.user.model

import java.util.UUID
import pl.illchess.player_info.domain.game.command.ObtainNewGame
import pl.illchess.player_info.domain.game.model.UserGameInfo
import pl.illchess.player_info.domain.user.command.CreateNewUser

class User(
    val id: UserId,
    val username: Username,
    val currentRanking: UserRankingPoints
) {
    fun recalculateRanking(command: ObtainNewGame): UserGameInfo {
        val previousRanking = currentRanking.copy()
        currentRanking.value = (Math.random() * 1000).toInt()
        val rankingPointsChange = currentRanking - previousRanking

        return UserGameInfo(
            this,
            previousRanking,
            currentRanking,
            rankingPointsChange
        )

    }

    companion object {
        fun createUser(command: CreateNewUser) = User(
            command.id ?: UserId(UUID.randomUUID()),
            command.username,
            UserRankingPoints.defaultRanking()
        )
    }
}
