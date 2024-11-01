package pl.illchess.player_info.domain.player.model

import kotlin.math.pow
import pl.illchess.player_info.domain.game.model.PlayerGameInfo

data class PlayerRankingPoints(var value: Int) {

    fun establishPlayerGameInfo(
        player: Player,
        enemyRP: PlayerRankingPoints,
        gameResultForUser: PlayerGameResult
    ): PlayerGameInfo {
        val expectedUserResult = obtainExpectedResult(enemyRP)

        val calculatedValue = value + K_FACTOR * (gameResultForUser.asNumber - expectedUserResult)
        val newRankingPoints = PlayerRankingPoints(calculatedValue.toInt())

        return PlayerGameInfo(
            player,
            this,
            newRankingPoints,
            newRankingPoints - this
        )
    }

    private fun obtainExpectedResult(
        enemyRP: PlayerRankingPoints
    ): Double {
        val qa = 10.0.pow(value / 400.0)
        val qb = 10.0.pow(enemyRP.value / 400.0)

        return qa.div(qa + qb)
    }

    operator fun plus(increment: PlayerRankingPoints): PlayerRankingPoints {
        return PlayerRankingPoints(value + increment.value)
    }

    operator fun minus(decrement: PlayerRankingPoints): PlayerRankingPoints {
        return PlayerRankingPoints(value - decrement.value)
    }

    companion object {
        const val K_FACTOR = 32
        fun defaultRanking() = PlayerRankingPoints(800)
    }

}