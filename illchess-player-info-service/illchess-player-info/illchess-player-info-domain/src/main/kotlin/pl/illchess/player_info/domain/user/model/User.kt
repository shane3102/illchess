package pl.illchess.player_info.domain.user.model

import pl.illchess.player_info.domain.game.command.ObtainNewGame

class User(
    val id: UserId,
    val username: Username,
    val currentRanking: UserRankingPoints
) {
    fun recalculateRanking(command: ObtainNewGame) {
        currentRanking.value = Math.random().toBigDecimal()
    }
}
