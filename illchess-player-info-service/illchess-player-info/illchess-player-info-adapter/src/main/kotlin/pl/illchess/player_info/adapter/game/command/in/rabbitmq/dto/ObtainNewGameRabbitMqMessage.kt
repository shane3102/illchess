package pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto

import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.LocalDateTime
import java.util.UUID
import pl.illchess.player_info.adapter.game.command.`in`.inbox.dto.ObtainNewGameInboxMessage
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase

@RegisterForReflection
data class ObtainNewGameRabbitMqMessage(
    val gameId: UUID,
    val whiteUsername: String,
    val blackUsername: String,
    val gameResult: String,
    val endTime: LocalDateTime,
    val performedMoves: List<PerformedMovesRabbitMqMessage>
) {
    @RegisterForReflection
    data class PerformedMovesRabbitMqMessage(
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

    fun toInboxMessage(): ObtainNewGameInboxMessage {
        return ObtainNewGameInboxMessage(
            gameId,
            whiteUsername,
            blackUsername,
            gameResult,
            endTime,
            performedMoves.map {
                ObtainNewGameInboxMessage.PerformedMoveInboxMessage(
                    it.startSquare,
                    it.endSquare,
                    it.stringValue,
                    it.color
                )
            }
        )
    }
}
