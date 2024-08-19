package pl.illchess.player_info.application

import pl.illchess.player_info.application.game.command.GameManager
import pl.illchess.player_info.application.game.command.out.LoadGame
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.application.test_impl.InMemoryGameRepository
import pl.illchess.player_info.application.test_impl.InMemoryUserRepository
import pl.illchess.player_info.application.user.command.out.LoadUser
import pl.illchess.player_info.application.user.command.out.SaveUser
import spock.lang.Specification

class UnitTestSpecification extends Specification {

    private def inMemoryUserRepository = new InMemoryUserRepository()
    private def inMemoryGameRepository = new InMemoryGameRepository()

    protected LoadUser loadUser = inMemoryUserRepository
    protected SaveUser saveUser = inMemoryUserRepository

    protected SaveGame saveGame = inMemoryGameRepository
    protected LoadGame loadGame = inMemoryGameRepository

    protected GameManager gameManager = new GameManager(
            saveGame,
            saveUser,
            loadUser
    )

    String generateRandomString() {
        return "RandomString${UUID.randomUUID()}"
    }
}
