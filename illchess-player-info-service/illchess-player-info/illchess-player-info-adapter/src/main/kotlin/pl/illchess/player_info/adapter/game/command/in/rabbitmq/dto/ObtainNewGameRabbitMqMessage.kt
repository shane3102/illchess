package pl.illchess.player_info.adapter.game.command.`in`.rabbitmq.dto

import pl.illchess.player_info.adapter.game.command.`in`.inbox.dto.ObtainNewGameInboxMessage
import pl.illchess.player_info.application.game.command.`in`.ObtainNewGameUseCase
import java.util.UUID

data class ObtainNewGameRabbitMqMessage(
    val gameId: UUID,
    val whiteUsername: String,
    val blackUsername: String,
    val winningPieceColor: String,
    val performedMoves: List<PerformedMovesRabbitMqMessage>
) {
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

    fun toInboxMessage(): ObtainNewGameInboxMessage {
        return ObtainNewGameInboxMessage(
            gameId,
            whiteUsername,
            blackUsername,
            winningPieceColor,
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
