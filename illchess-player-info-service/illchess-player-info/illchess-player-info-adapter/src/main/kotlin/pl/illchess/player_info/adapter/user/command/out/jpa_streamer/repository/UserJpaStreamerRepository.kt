package pl.illchess.player_info.adapter.user.command.out.jpa_streamer.repository

import com.speedment.jpastreamer.application.JPAStreamer
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.user.command.out.jpa_streamer.entity.UserEntity
import java.util.UUID

@ApplicationScoped
class UserJpaStreamerRepository(
    private val jpaStreamer: JPAStreamer
) {
    fun loadById(id: UUID): UserEntity? {
        return jpaStreamer.stream(UserEntity::class.java)
            .filter { it.id == id }
            .findFirst()
            .orElse(null)
    }

    fun loadByUsername(username: String): UserEntity? {
        return jpaStreamer.stream(UserEntity::class.java)
            .filter { it.username == username }
            .findFirst()
            .orElse(null)
    }

    fun save(userEntity: UserEntity) {
        userEntity.persist()
    }
}