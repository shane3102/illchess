package pl.illchess.player_info.application.game.command

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.player_info.application.commons.command.out.PublishEvent
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase.ObtainNewGameCmd
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.application.user.command.out.LoadUser
import pl.illchess.player_info.application.user.command.out.SaveUser
import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.illchess.player_info.domain.game.event.ErrorWhileSavingGameEvent
import pl.illchess.player_info.domain.game.event.GameSavedEvent
import pl.illchess.player_info.domain.game.model.Game
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.user.exception.UserNotFoundException
import pl.illchess.player_info.domain.user.model.User
import pl.illchess.player_info.domain.user.model.Username

class GameManager(
    private val saveGame: SaveGame,
    private val saveUser: SaveUser,
    private val loadUser: LoadUser,
    private val publishEvent: PublishEvent
) : ObtainNewGameUseCase {


    override fun obtainNewGame(cmd: ObtainNewGameCmd) {
        try {
            log.info(
                """
                Obtaining new game with id ${cmd.id} of players with given usernames: 
                WHITE - ${cmd.whiteUsername}, BLACK - ${cmd.blackUsername} won by ${cmd.winningPieceColor} player. 
                Recalculating players ranking points and saving new game to database.
                """.trimIndent().replace(Regex("(\n*)\n"), "$1")
            )
            val whiteUsername = Username(cmd.whiteUsername)
            val whiteUser: User = loadUser.load(whiteUsername) ?: throw UserNotFoundException(whiteUsername)
            val blackUsername = Username(cmd.blackUsername)
            val blackUser: User = loadUser.load(blackUsername) ?: throw UserNotFoundException(blackUsername)
            val command = cmd.toCommand(whiteUser, blackUser)

            val game = Game.generateNewGame(command)

            saveGame.save(game)
            saveUser.save(whiteUser)
            saveUser.save(blackUser)

            log.info(
                """
                Successfully saved new game with id ${cmd.id} to database and 
                recalculated ranking points of users ${cmd.whiteUsername} and ${cmd.blackUsername}.
                """.trimIndent().replace(Regex("(\n*)\n"), "$1")
            )

            publishEvent.publish(GameSavedEvent(game.id))
        } catch (d: DomainException) {
            log.error("Error while saving game with id ${cmd.id}")
            publishEvent.publish(ErrorWhileSavingGameEvent(GameId(cmd.id)))
            throw d
        }
    }
}

private val log: Logger = LoggerFactory.getLogger(ObtainNewGameUseCase::class.java)