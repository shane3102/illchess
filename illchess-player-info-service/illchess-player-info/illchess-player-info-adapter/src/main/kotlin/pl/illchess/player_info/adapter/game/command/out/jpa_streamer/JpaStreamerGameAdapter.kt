package pl.illchess.player_info.adapter.game.command.out.jpa_streamer

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import pl.illchess.player_info.adapter.game.command.out.jpa_streamer.mapper.GameMapper
import pl.illchess.player_info.adapter.game.command.out.jpa_streamer.repository.GameJpaStreamerRepository
import pl.illchess.player_info.application.game.command.out.DeleteGame
import pl.illchess.player_info.application.game.command.out.LoadGame
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.domain.game.model.Game
import pl.illchess.player_info.domain.game.model.GameId

@ApplicationScoped
class JpaStreamerGameAdapter(
    private val repository: GameJpaStreamerRepository
) : LoadGame, SaveGame, DeleteGame {
    override fun load(id: GameId): Game? {
        val entity = repository.loadById(id.uuid)
        return if (entity != null) GameMapper.toModel(entity) else null
    }

    @Transactional
    override fun save(game: Game) {
        repository.saveById(GameMapper.toEntity(game))
    }

    @Transactional
    override fun deleteGame(gameId: GameId) {
        repository.deleteById(gameId.uuid)
    }
}