package pl.illchess.player_info.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.player_info.application.player.command.out.CreatePlayer
import pl.illchess.player_info.application.player.command.out.DeletePlayer
import pl.illchess.player_info.application.player.command.out.LoadPlayer
import pl.illchess.player_info.application.player.command.out.SavePlayer
import pl.illchess.player_info.domain.player.command.CreateNewPlayer
import pl.illchess.player_info.domain.player.model.Player
import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.Username

class InMemoryPlayerRepository implements LoadPlayer, SavePlayer, DeletePlayer, CreatePlayer {

    private def repo = new HashMap<PlayerId, Player>()

    @Override
    Player load(@NotNull PlayerId id) {
        return repo.get(id)
    }

    @Override
    Player load(@NotNull Username username) {
        return repo.values().findAll { it.username == username }.first()
    }

    @Override
    void save(@NotNull Player user) {
        repo.put(user.id, user)
    }

    @Override
    void deleteUser(@NotNull PlayerId userId) {
        repo.remove(userId)
    }

    @Override
    PlayerId create(@NotNull Username username) {
        def user = Player.@Companion.createPlayer(new CreateNewPlayer(new PlayerId(UUID.randomUUID()), username))
        repo.put(user.id, user)
        user.id
    }
}
