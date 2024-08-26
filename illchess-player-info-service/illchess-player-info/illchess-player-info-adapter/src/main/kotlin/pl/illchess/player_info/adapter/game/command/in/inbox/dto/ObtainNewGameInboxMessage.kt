package pl.illchess.player_info.adapter.game.command.`in`.inbox.dto

import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import pl.messaging.model.Message
import java.time.OffsetDateTime
import java.util.UUID

data class ObtainNewGameInboxMessage(
    val gameId: UUID,
    val whiteUsername: String,
    val blackUsername: String,
    val winningPieceColor: String,
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
            winningPieceColor,
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

