package pl.illchess.player_info.application.game.command.out

import pl.illchess.player_info.domain.game.model.Game

interface SaveGame {
    fun save(game: Game)
}