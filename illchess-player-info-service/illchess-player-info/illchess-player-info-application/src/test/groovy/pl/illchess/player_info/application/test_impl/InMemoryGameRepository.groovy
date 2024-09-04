package pl.illchess.player_info.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.player_info.application.game.command.out.DeleteGame
import pl.illchess.player_info.application.game.command.out.LoadGame
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.domain.game.model.Game
import pl.illchess.player_info.domain.game.model.GameId

class InMemoryGameRepository implements SaveGame, LoadGame, DeleteGame {

    private def repo = new HashMap<GameId, Game>()

    @Override
    void save(@NotNull Game game) {
        repo.put(game.id, game)
    }

    @Override
    Game load(@NotNull GameId id) {
        return repo.get(id)
    }

    @Override
    void deleteGame(@NotNull GameId gameId) {
        repo.remove(gameId)
    }
}
