package pl.illchess.player_info.adapter.user.command.out.in_memory

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.player_info.application.user.command.out.LoadUser
import pl.illchess.player_info.application.user.command.out.SaveUser
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

@ApplicationScoped
class InMemoryUserRepository : LoadUser, SaveUser {

    private val repo: HashMap<UserId, User> = HashMap()

    override fun load(id: UserId): User? {
        return repo[id]
    }

    override fun load(username: Username): User? {
        return repo.values.firstOrNull { it.username == username }
    }

    override fun save(user: User) {
        repo[user.id] = user
    }
}