package pl.illchess.player_info.application.game.query.out

import pl.illchess.player_info.application.game.query.out.model.GameView
import java.util.UUID

interface GameViewQueryPort {

    fun findById(id: UUID): GameView?
}