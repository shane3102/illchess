package pl.illchess.player_info.adapter.user.command.out.jpa_streamer

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import pl.illchess.player_info.adapter.user.command.out.jpa_streamer.mapper.UserMapper
import pl.illchess.player_info.adapter.user.command.out.jpa_streamer.repository.UserJpaStreamerRepository
import pl.illchess.player_info.application.user.command.out.CreateUser
import pl.illchess.player_info.application.user.command.out.DeleteUser
import pl.illchess.player_info.application.user.command.out.LoadUser
import pl.illchess.player_info.application.user.command.out.SaveUser
import pl.illchess.player_info.domain.user.command.CreateNewUser
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.UserId
import pl.illchess.player_info.domain.user.model.Username

@ApplicationScoped
class JpaStreamerUserAdapter(
    private val repository: UserJpaStreamerRepository
) : LoadUser, SaveUser, DeleteUser, CreateUser {
    override fun load(id: UserId): User? {
        val entity = repository.loadById(id.uuid)
        return if (entity != null) UserMapper.toModel(entity) else null
    }

    override fun load(username: Username): User? {
        val entity = repository.loadByUsername(username.text)
        return if (entity != null) UserMapper.toModel(entity) else null
    }

    @Transactional
    override fun save(user: User) {
        repository.save(UserMapper.toEntity(user))
    }

    @Transactional
    override fun deleteUser(userId: UserId) {
        repository.deleteById(userId.uuid)
    }

    @Transactional
    override fun create(username: Username): UserId {
        val createdUser = User.createUser(
            CreateNewUser(username = username)
        )
        repository.save(UserMapper.toEntity(createdUser))
        return createdUser.id
    }
}