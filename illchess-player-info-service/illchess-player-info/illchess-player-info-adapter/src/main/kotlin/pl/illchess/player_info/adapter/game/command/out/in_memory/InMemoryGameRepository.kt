package pl.illchess.player_info.adapter.game.command.out.in_memory

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.application.game.command.out.LoadGame
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.domain.game.model.Game
import pl.illchess.player_info.domain.game.model.GameId

@ApplicationScoped
class InMemoryGameRepository : LoadGame, SaveGame {

    private val repo: HashMap<GameId, Game> = HashMap()

    override fun load(id: GameId): Game? {
        return repo[id]
    }

    override fun save(game: Game) {
        repo[game.id] = game
    }
}