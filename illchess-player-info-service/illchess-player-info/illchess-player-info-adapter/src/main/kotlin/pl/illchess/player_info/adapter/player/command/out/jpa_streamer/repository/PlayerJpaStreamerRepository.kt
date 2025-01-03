package pl.illchess.player_info.adapter.player.command.out.jpa_streamer.repository

import com.speedment.jpastreamer.application.JPAStreamer
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.shared_entities.PlayerEntity
import pl.illchess.player_info.adapter.shared_entities.PlayerEntityMetaModel
import java.util.UUID

@ApplicationScoped
class PlayerJpaStreamerRepository(
    private val jpaStreamer: JPAStreamer
) : PanacheRepositoryBase<PlayerEntity, UUID> {
    fun loadById(id: UUID): PlayerEntity? {
        return jpaStreamer.stream(PlayerEntity::class.java)
            .filter(PlayerEntityMetaModel.id.`in`(id))
            .findFirst()
            .orElse(null)
    }

    fun loadByUsername(username: String): PlayerEntity? {
        return jpaStreamer.stream(PlayerEntity::class.java)
            .filter(PlayerEntityMetaModel.username.`in`(username))
            .findFirst()
            .orElse(null)
    }

    fun save(userEntity: PlayerEntity) {
        if (this.findById(userEntity.id) != null) {
            this.entityManager.merge(userEntity)
        } else {
            userEntity.persist()
        }
    }
}