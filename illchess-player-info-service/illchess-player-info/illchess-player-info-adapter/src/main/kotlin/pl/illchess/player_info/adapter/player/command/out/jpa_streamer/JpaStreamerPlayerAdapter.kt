package pl.illchess.player_info.adapter.player.command.out.jpa_streamer

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import pl.illchess.player_info.adapter.player.command.out.jpa_streamer.mapper.PlayerMapper
import pl.illchess.player_info.adapter.player.command.out.jpa_streamer.repository.PlayerJpaStreamerRepository
import pl.illchess.player_info.application.player.command.out.CreatePlayer
import pl.illchess.player_info.application.player.command.out.DeletePlayer
import pl.illchess.player_info.application.player.command.out.LoadPlayer
import pl.illchess.player_info.application.player.command.out.SavePlayer
import pl.illchess.player_info.domain.player.command.CreateNewPlayer
import pl.illchess.player_info.domain.player.model.Player
import pl.illchess.player_info.domain.player.model.PlayerId
import pl.illchess.player_info.domain.player.model.Username

@ApplicationScoped
class JpaStreamerPlayerAdapter(
    private val repository: PlayerJpaStreamerRepository
) : LoadPlayer, SavePlayer, DeletePlayer, CreatePlayer {
    override fun load(id: PlayerId): Player? {
        val entity = repository.loadById(id.uuid)
        return if (entity != null) PlayerMapper.toModel(entity) else null
    }

    override fun load(username: Username): Player? {
        val entity = repository.loadByUsername(username.text)
        return if (entity != null) PlayerMapper.toModel(entity) else null
    }

    @Transactional
    override fun save(player: Player) {
        repository.save(PlayerMapper.toEntity(player))
    }

    @Transactional
    override fun deleteUser(playerId: PlayerId) {
        repository.deleteById(playerId.uuid)
    }

    @Transactional
    override fun create(username: Username): PlayerId {
        val createdPlayer = Player.createPlayer(
            CreateNewPlayer(username = username)
        )
        repository.save(PlayerMapper.toEntity(createdPlayer))
        return createdPlayer.id
    }
}