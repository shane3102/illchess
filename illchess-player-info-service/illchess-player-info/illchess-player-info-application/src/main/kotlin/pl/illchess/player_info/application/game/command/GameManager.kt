package pl.illchess.player_info.application.game.command

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.player_info.application.commons.command.out.PublishEvent
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase.ObtainNewGameCmd
import pl.illchess.player_info.application.game.command.out.LoadGame
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.application.player.command.out.CreatePlayer
import pl.illchess.player_info.application.player.command.out.LoadPlayer
import pl.illchess.player_info.application.player.command.out.SavePlayer
import pl.illchess.player_info.domain.commons.exception.DomainException
import pl.illchess.player_info.domain.game.event.ErrorWhileSavingGameEvent
import pl.illchess.player_info.domain.game.event.GameSavedEvent
import pl.illchess.player_info.domain.game.exception.GameAlreadyExistsException
import pl.illchess.player_info.domain.game.model.Game
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.player.exception.PlayerNotFoundException
import pl.illchess.player_info.domain.player.model.Player
import pl.illchess.player_info.domain.player.model.Username

class GameManager(
    private val saveGame: SaveGame,
    private val loadGame: LoadGame,
    private val savePlayer: SavePlayer,
    private val loadPlayer: LoadPlayer,
    private val createPlayer: CreatePlayer,
    private val publishEvent: PublishEvent
) : ObtainNewGameUseCase {

    override fun obtainNewGame(cmd: ObtainNewGameCmd) {
        try {
            log.info(
                """
                Obtaining new game with id ${cmd.id} of players with given usernames: 
                WHITE - ${cmd.whiteUsername}, BLACK - ${cmd.blackUsername} with result - ${cmd.gameResult} and result cause ${cmd.gameResultCause} 
                Recalculating players ranking points and saving new game to database.
                """.trimIndent().replace(Regex("(\n*)\n"), "$1")
            )
            val gameId = GameId(cmd.id)
            val gameWithSameId = loadGame.load(gameId)
            if (gameWithSameId != null) {
                log.warn("Game with id = ${cmd.id} already exists. Skipping saving game and calculating players points")
                throw GameAlreadyExistsException(gameId)
            }
            val whiteUsername = Username(cmd.whiteUsername)
            val whitePlayer: Player = loadOrCreateAndLoadUser(whiteUsername)
            val blackUsername = Username(cmd.blackUsername)
            val blackPlayer: Player = loadOrCreateAndLoadUser(blackUsername)
            val command = cmd.toCommand(whitePlayer, blackPlayer)

            val game = Game.generateNewGame(command)

            saveGame.save(game)
            savePlayer.save(whitePlayer)
            savePlayer.save(blackPlayer)

            log.info(
                """
                Successfully saved new game with id ${cmd.id} to database and 
                recalculated ranking points of users ${cmd.whiteUsername} and ${cmd.blackUsername}.
                """.trimIndent().replace(Regex("(\n*)\n"), "$1")
            )

            publishEvent.publish("game.saved", GameSavedEvent(game.id))
        } catch (e: GameAlreadyExistsException) {
            log.info("Game with id: ${cmd.id} already saved")
            publishEvent.publish("game.saved", GameSavedEvent(GameId(cmd.id)))
        } catch (d: DomainException) {
            log.error("Error while saving game with id ${cmd.id}")
            publishEvent.publish("game.error", ErrorWhileSavingGameEvent(GameId(cmd.id)))
            throw d
        }
    }

    private fun loadOrCreateAndLoadUser(username: Username): Player {
        var user = loadPlayer.load(username)
        if (user != null) {
            return user
        }
        val createdUserId = createPlayer.create(username)
        user = loadPlayer.load(createdUserId) ?: throw PlayerNotFoundException(createdUserId)
        return user
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ObtainNewGameUseCase::class.java)
    }

}