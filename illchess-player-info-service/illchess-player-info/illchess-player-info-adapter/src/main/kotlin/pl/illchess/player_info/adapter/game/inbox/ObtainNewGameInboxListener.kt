package pl.illchess.player_info.adapter.game.inbox

import pl.illchess.player_info.adapter.game.inbox.dto.ObtainNewGameInboxMessage
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase.ObtainNewGameCmd
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase.PerformedMoveCmd
import pl.messaging.annotation.InboxOutboxListener
import pl.messaging.quarkus.runtime.annotation.MessagingAwareComponent

@MessagingAwareComponent
class ObtainNewGameInboxListener(
    private val obtainNewGameUseCase: ObtainNewGameUseCase
) {

    @InboxOutboxListener(
        retryCount = 100,
        batchSize = 20,
        cron = "*/5 * * * * ?"
    )
    fun obtainNewGame(inboxMessage: ObtainNewGameInboxMessage) {
        obtainNewGameUseCase.obtainNewGame(
            ObtainNewGameCmd(
                inboxMessage.gameId,
                inboxMessage.whiteUsername,
                inboxMessage.blackUsername,
                inboxMessage.winningPieceColor,
                inboxMessage.performedMoves.map {
                    PerformedMoveCmd(
                        it.startSquare,
                        it.endSquare,
                        it.stringValue,
                        it.color
                    )
                }
            )
        )
    }

}