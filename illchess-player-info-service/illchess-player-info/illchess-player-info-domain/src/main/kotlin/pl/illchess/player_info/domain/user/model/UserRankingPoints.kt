package pl.illchess.player_info.domain.user.model

import kotlin.math.pow
import pl.illchess.player_info.domain.game.model.UserGameInfo

data class UserRankingPoints(var value: Int) {

    fun establishUserGameInfo(
        user: User,
        enemyRP: UserRankingPoints,
        gameResultForUser: UserGameResult
    ): UserGameInfo {
        val expectedUserResult = obtainExpectedResult(enemyRP)

        val calculatedValue = value + K_FACTOR * (gameResultForUser.asNumber - expectedUserResult)
        val newRankingPoints = UserRankingPoints(calculatedValue.toInt())

        return UserGameInfo(
            user,
            this,
            newRankingPoints,
            newRankingPoints - this
        )
    }

    private fun obtainExpectedResult(
        enemyRP: UserRankingPoints
    ): Double {
        val qa = 10.0.pow(value / 400.0)
        val qb = 10.0.pow(enemyRP.value / 400.0)

        return qa.div(qa + qb)
    }

    operator fun plus(increment: UserRankingPoints): UserRankingPoints {
        return UserRankingPoints(value + increment.value)
    }

    operator fun minus(decrement: UserRankingPoints): UserRankingPoints {
        return UserRankingPoints(value - decrement.value)
    }

    companion object {
        const val K_FACTOR = 32
        fun defaultRanking() = UserRankingPoints(800)
    }

}