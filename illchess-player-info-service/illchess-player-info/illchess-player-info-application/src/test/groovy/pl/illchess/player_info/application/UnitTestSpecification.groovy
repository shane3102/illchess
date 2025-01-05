package pl.illchess.player_info.application

import pl.illchess.player_info.application.commons.command.out.PublishEvent
import pl.illchess.player_info.application.game.command.GameManager
import pl.illchess.player_info.application.game.command.out.LoadGame
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.application.test_impl.InMemoryGameRepository
import pl.illchess.player_info.application.test_impl.InMemoryPlayerRepository
import pl.illchess.player_info.application.test_impl.TestPublishEvent
import pl.illchess.player_info.application.player.command.out.CreatePlayer
import pl.illchess.player_info.application.player.command.out.LoadPlayer
import pl.illchess.player_info.application.player.command.out.SavePlayer
import spock.lang.Specification

class UnitTestSpecification extends Specification {

    private def inMemoryUserRepository = new InMemoryPlayerRepository()
    private def inMemoryGameRepository = new InMemoryGameRepository()

    protected LoadPlayer loadUser = inMemoryUserRepository
    protected SavePlayer saveUser = inMemoryUserRepository
    protected CreatePlayer createUser = inMemoryUserRepository

    protected SaveGame saveGame = inMemoryGameRepository
    protected LoadGame loadGame = inMemoryGameRepository

    protected PublishEvent publishEvent = new TestPublishEvent()

    protected GameManager gameManager = new GameManager(
            saveGame,
            loadGame,
            saveUser,
            loadUser,
            createUser,
            publishEvent
    )

    String generateRandomString() {
        return "RandomString${UUID.randomUUID()}"
    }
}
