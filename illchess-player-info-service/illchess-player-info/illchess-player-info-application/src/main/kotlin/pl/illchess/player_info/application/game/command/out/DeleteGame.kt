package pl.illchess.player_info.application.game.command.out

import pl.illchess.player_info.domain.game.model.GameId

interface DeleteGame {
    fun deleteGame(gameId: GameId)
}