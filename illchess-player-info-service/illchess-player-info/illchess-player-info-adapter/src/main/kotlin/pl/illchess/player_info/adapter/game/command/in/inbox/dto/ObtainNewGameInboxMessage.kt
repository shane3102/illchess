package pl.illchess.player_info.adapter.game.command.`in`.inbox.dto

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.UUID
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.shane3102.messaging.model.Message

data class ObtainNewGameInboxMessage(
    val gameId: UUID,
    val whiteUsername: String,
    val blackUsername: String,
    val gameResult: String,
    val endTime: LocalDateTime,
    val performedMoves: List<PerformedMoveInboxMessage>
) : Message(UUID.randomUUID(), 0, OffsetDateTime.now()) {

    data class PerformedMoveInboxMessage(
        val startSquare: String,
        val endSquare: String,
        val stringValue: String,
        val color: String
    )

    fun toCmd(): ObtainNewGameUseCase.ObtainNewGameCmd {
        return ObtainNewGameUseCase.ObtainNewGameCmd(
            gameId,
            whiteUsername,
            blackUsername,
            gameResult,
            endTime,
            performedMoves.map {
                ObtainNewGameUseCase.PerformedMoveCmd(
                    it.startSquare,
                    it.endSquare,
                    it.stringValue,
                    it.color
                )
            }
        )
    }

}

