package pl.illchess.player_info.adapter.player.command.out.jpa_streamer.mapper

import pl.illchess.player_info.adapter.shared_entities.PlayerEntity
import pl.illchess.player_info.domain.player.model.Player
import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.PlayerRankingPoints
import pl.illchess.player_info.domain.player.model.Username

class PlayerMapper {
    companion object {

        fun toEntity(player: Player) = PlayerEntity(
            player.id.uuid,
            player.username.text,
            player.currentRanking.value
        )

        fun toModel(userEntity: PlayerEntity) = Player(
            PlayerId(userEntity.id),
            Username(userEntity.username),
            PlayerRankingPoints(userEntity.currentRankingPoints)
        )
    }
}