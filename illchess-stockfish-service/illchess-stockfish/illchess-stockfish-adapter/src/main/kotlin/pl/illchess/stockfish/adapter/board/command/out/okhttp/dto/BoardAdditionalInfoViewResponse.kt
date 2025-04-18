package pl.illchess.stockfish.adapter.board.command.out.okhttp.dto

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.UUID

@RegisterForReflection
data class BoardAdditionalInfoViewResponse(
    val gameId: UUID,
    val currentPlayerColor: String,
    val whitePlayer: PlayerView,
    val blackPlayer: PlayerView?,
    val gameState: String,
    val victoriousPlayerColor: String?
) {
    @RegisterForReflection
    data class PlayerView(
        val username: String
    )
}
