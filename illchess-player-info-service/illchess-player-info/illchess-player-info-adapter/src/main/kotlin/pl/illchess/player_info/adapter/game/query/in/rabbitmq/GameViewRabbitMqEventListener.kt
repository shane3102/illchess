package pl.illchess.player_info.adapter.game.query.`in`.rabbitmq

import pl.illchess.player_info.application.game.query.out.model.GameView
import pl.illchess.player_info.domain.game.event.ErrorWhileSavingGameEvent
import pl.illchess.player_info.domain.game.event.GameSavedEvent
import java.util.UUID

interface GameViewRabbitMqEventListener {

    fun sendGameViewOnGameSaved(gameSavedEvent: GameSavedEvent): GameView

    fun sendErrorInfoOnErrorWhileSavingGame(errorWhileSavingGameEvent: ErrorWhileSavingGameEvent): UUID

}