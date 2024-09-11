package pl.illchess.player_info.application.test_impl

import org.jetbrains.annotations.NotNull
import pl.illchess.player_info.application.user.command.out.CreateUser
import pl.illchess.player_info.application.user.command.out.DeleteUser
import pl.illchess.player_info.application.user.command.out.LoadUser
import pl.illchess.player_info.application.user.command.out.SaveUser
import pl.illchess.player_info.domain.user.command.CreateNewUser
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

class InMemoryUserRepository implements LoadUser, SaveUser, DeleteUser, CreateUser {

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

    @Override
    void deleteUser(@NotNull UserId userId) {
        repo.remove(userId)
    }

    @Override
    UserId create(@NotNull Username username) {
        def user = User.@Companion.createUser(new CreateNewUser(new UserId(UUID.randomUUID()), username))
        repo.put(user.id, user)
        user.id
    }
}
