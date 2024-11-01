package pl.illchess.player_info.application.player

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.player_info.application.player.command.`in`.CreateNewPlayerUseCase
import pl.illchess.player_info.application.player.command.`in`.CreateNewPlayerUseCase.CreateNewPlayerCmd
import pl.illchess.player_info.application.player.command.out.SavePlayer
import pl.illchess.player_info.domain.player.model.Player

class PlayerManager(
    private val savePlayer: SavePlayer
) : CreateNewPlayerUseCase {
    override fun createNewPlayer(cmd: CreateNewPlayerCmd) {
        log.info("Creating new user with given data: $cmd")
        val command = cmd.toCommand()
        val createdPlayer = Player.createPlayer(command)
        savePlayer.save(createdPlayer)
        log.info("Successfully created user with given data: $cmd")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PlayerManager::class.java)
    }
}