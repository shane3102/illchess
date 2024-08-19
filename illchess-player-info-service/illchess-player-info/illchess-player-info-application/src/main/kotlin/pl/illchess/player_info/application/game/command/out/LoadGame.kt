package pl.illchess.player_info.application.game.command.out

import pl.illchess.player_info.domain.game.model.Game
import pl.illchess.player_info.domain.game.model.GameId

interface LoadGame {
    fun load(id: GameId): Game?
}