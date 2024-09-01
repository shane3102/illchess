package pl.illchess.player_info.adapter.game.command.out.jpa_streamer.repository

import com.speedment.jpastreamer.application.JPAStreamer
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.shared_entities.GameEntity
import pl.illchess.player_info.adapter.shared_entities.GameEntityMetaModel
import java.util.UUID

@ApplicationScoped
class GameJpaStreamerRepository(private val jpaStreamer: JPAStreamer) {

    fun loadById(id: UUID): GameEntity? {
        return jpaStreamer.stream(GameEntity::class.java)
            .filter(GameEntityMetaModel.id.equal(id))
            .findFirst()
            .orElse(null)
    }

    fun saveById(gameEntity: GameEntity) {
        gameEntity.persist()
    }

}