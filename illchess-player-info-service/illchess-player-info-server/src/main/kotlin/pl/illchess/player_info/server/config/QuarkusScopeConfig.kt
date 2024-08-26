package pl.illchess.player_info.server.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import pl.illchess.player_info.application.commons.command.out.PublishEvent
import pl.illchess.player_info.application.game.command.GameManager
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.application.user.command.out.LoadUser
import pl.illchess.player_info.application.user.command.out.SaveUser

class QuarkusScopeConfig {

    @Produces
    @ApplicationScoped
    fun gameManager(
        saveGame: SaveGame,
        saveUser: SaveUser,
        loadUser: LoadUser,
        publishEvent: PublishEvent
    ) = GameManager(saveGame, saveUser, loadUser, publishEvent)

}