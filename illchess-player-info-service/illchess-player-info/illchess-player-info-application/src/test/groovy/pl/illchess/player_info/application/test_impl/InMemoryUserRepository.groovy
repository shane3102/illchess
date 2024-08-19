package pl.illchess.player_info.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.player_info.application.user.command.out.LoadUser
import pl.illchess.player_info.application.user.command.out.SaveUser
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

class InMemoryUserRepository implements LoadUser, SaveUser {

    private def repo = new HashMap<UserId, User>()

    @Override
    User load(@NotNull UserId id) {
        return repo.get(id)
    }

    @Override
    User load(@NotNull Username username) {
        return repo.values().findAll { it.username == username }.first()
    }

    @Override
    void save(@NotNull User user) {
        repo.put(user.id, user)
    }
}
