package pl.illchess.player_info.adapter.user.command.out.jpa_streamer.repository

import com.speedment.jpastreamer.application.JPAStreamer
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.shared_entities.UserEntity
import pl.illchess.player_info.adapter.shared_entities.UserEntityMetaModel
import java.util.UUID

@ApplicationScoped
class UserJpaStreamerRepository(
    private val jpaStreamer: JPAStreamer
) {
    fun loadById(id: UUID): UserEntity? {
        return jpaStreamer.stream(UserEntity::class.java)
            .filter(UserEntityMetaModel.id.equal(id))
            .findFirst()
            .orElse(null)
    }

    fun loadByUsername(username: String): UserEntity? {
        return jpaStreamer.stream(UserEntity::class.java)
            .filter(UserEntityMetaModel.username.equal(username))
            .findFirst()
            .orElse(null)
    }

    fun save(userEntity: UserEntity) {
        userEntity.persist()
    }
}