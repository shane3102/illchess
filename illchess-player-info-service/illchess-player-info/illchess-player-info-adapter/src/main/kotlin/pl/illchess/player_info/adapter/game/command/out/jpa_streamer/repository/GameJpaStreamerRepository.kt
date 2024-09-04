package pl.illchess.player_info.adapter.game.command.out.jpa_streamer.repository

import com.speedment.jpastreamer.application.JPAStreamer
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.shared_entities.GameEntity
import pl.illchess.player_info.adapter.shared_entities.GameEntityMetaModel
import java.util.UUID

@ApplicationScoped
class GameJpaStreamerRepository(private val jpaStreamer: JPAStreamer) : PanacheRepositoryBase<GameEntity, UUID> {

    fun loadById(id: UUID): GameEntity? {
        return jpaStreamer.stream(GameEntity::class.java)
            .filter(GameEntityMetaModel.id.equal(id))
            .findFirst()
            .orElse(null)
    }

    fun saveById(gameEntity: GameEntity) {
        if (GameEntity.findById<GameEntity>(gameEntity.id) != null) {
            this.entityManager.merge(gameEntity)
        } else {
            gameEntity.persist()
        }
    }

}