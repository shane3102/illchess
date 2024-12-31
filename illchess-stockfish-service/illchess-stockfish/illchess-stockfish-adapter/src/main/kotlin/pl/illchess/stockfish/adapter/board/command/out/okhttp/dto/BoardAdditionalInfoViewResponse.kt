package pl.illchess.stockfish.adapter.board.command.out.okhttp.dto

import java.util.UUID

data class BoardAdditionalInfoViewResponse(
    val boardId: UUID,
    val currentPlayerColor: String,
    val whitePlayer: PlayerView,
    val blackPlayer: PlayerView?,
    val gameState: String,
    val victoriousPlayerColor: String?
) {
    data class PlayerView(
        val username: String
    )
}
