package pl.illchess.player_info.adapter.game.query.`in`.rest

import pl.illchess.player_info.application.game.query.out.GameViewQueryPort
import pl.illchess.player_info.domain.game.exception.GameNotFoundException
import pl.illchess.player_info.domain.game.model.GameId
import java.util.UUID

class GameViewController(private val gameViewQueryPort: GameViewQueryPort) : GameViewApi {

    override fun getGameById(gameId: UUID) = gameViewQueryPort.findById(gameId)
        ?: throw GameNotFoundException(GameId(gameId))
}