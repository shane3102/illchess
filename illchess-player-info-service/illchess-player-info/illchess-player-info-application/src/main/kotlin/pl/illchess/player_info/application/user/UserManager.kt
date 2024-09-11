package pl.illchess.player_info.application.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.player_info.application.user.command.`in`.CreateNewUserUseCase
import pl.illchess.player_info.application.user.command.`in`.CreateNewUserUseCase.CreateNewUserCmd
import pl.illchess.player_info.application.user.command.out.SaveUser
import pl.illchess.player_info.domain.user.model.User

class UserManager(
    private val saveUser: SaveUser
) : CreateNewUserUseCase {
    override fun createNewUser(cmd: CreateNewUserCmd) {
        log.info("Creating new user with given data: $cmd")
        val command = cmd.toCommand()
        val createdUser = User.createUser(command)
        saveUser.save(createdUser)
        log.info("Successfully created user with given data: $cmd")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserManager::class.java)
    }
}