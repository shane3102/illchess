package pl.illchess.player_info.domain.user.model

data class UserRankingPoints(var value: Int) {
    operator fun plus(increment: UserRankingPoints): UserRankingPoints {
        return UserRankingPoints(value + increment.value)
    }

    operator fun minus(decrement: UserRankingPoints): UserRankingPoints {
        return UserRankingPoints(value - decrement.value)
    }

}