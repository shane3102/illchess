package pl.illchess.player_info.adapter.game.command.out.jpa_streamer.repository

import com.speedment.jpastreamer.application.JPAStreamer
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.adapter.game.command.out.jpa_streamer.entity.GameEntity
import java.util.UUID

@ApplicationScoped
class GameJpaStreamerRepository(private val jpaStreamer: JPAStreamer) {

    fun loadById(id: UUID): GameEntity? {
        return jpaStreamer.stream(GameEntity::class.java)
            .filter { it.id == id }
            .findFirst()
            .orElse(null)
    }

    fun saveById(gameEntity: GameEntity) {
        gameEntity.persist()
    }

}