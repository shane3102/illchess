package pl.illchess.player_info.domain.player.model

import java.util.UUID
import pl.illchess.player_info.domain.game.model.PlayerGameInfo
import pl.illchess.player_info.domain.player.command.CreateNewPlayer

class Player(
    val id: PlayerId,
    val username: Username,
    var currentRanking: PlayerRankingPoints
) {
    fun applyNewRanking(playerGameInfo: PlayerGameInfo) {
        currentRanking = playerGameInfo.rankingPointsAfterGame
    }

    companion object {
        fun createPlayer(command: CreateNewPlayer) = Player(
            command.id ?: PlayerId(UUID.randomUUID()),
            command.username,
            PlayerRankingPoints.defaultRanking()
        )
    }
}
