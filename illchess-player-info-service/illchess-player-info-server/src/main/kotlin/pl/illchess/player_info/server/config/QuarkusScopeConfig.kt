package pl.illchess.player_info.server.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import pl.illchess.player_info.application.commons.command.out.PublishEvent
import pl.illchess.player_info.application.game.command.GameManager
import pl.illchess.player_info.application.game.command.out.SaveGame
import pl.illchess.player_info.application.player.PlayerManager
import pl.illchess.player_info.application.player.command.out.CreatePlayer
import pl.illchess.player_info.application.player.command.out.LoadPlayer
import pl.illchess.player_info.application.player.command.out.SavePlayer

class QuarkusScopeConfig {

    @Produces
    @ApplicationScoped
    fun gameManager(
        saveGame: SaveGame,
        savePlayer: SavePlayer,
        loadPlayer: LoadPlayer,
        createPlayer: CreatePlayer,
        publishEvent: PublishEvent
    ) = GameManager(saveGame, savePlayer, loadPlayer, createPlayer, publishEvent)

    @Produces
    @ApplicationScoped
    fun userManager(savePlayer: SavePlayer) = PlayerManager(savePlayer)

}