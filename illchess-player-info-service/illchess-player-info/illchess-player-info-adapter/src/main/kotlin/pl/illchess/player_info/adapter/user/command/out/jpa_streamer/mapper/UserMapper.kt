package pl.illchess.player_info.adapter.user.command.out.jpa_streamer.mapper

import pl.illchess.player_info.adapter.shared_entities.UserEntity
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.UserRankingPoints
import pl.illchess.player_info.domain.user.model.Username

class UserMapper {
    companion object {

        fun toEntity(user: User) = UserEntity(
            user.id.uuid,
            user.username.text,
            user.currentRanking.value
        )

        fun toModel(userEntity: UserEntity) = User(
            UserId(userEntity.id),
            Username(userEntity.username),
            UserRankingPoints(userEntity.currentRankingPoints)
        )
    }
}