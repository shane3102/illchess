package pl.illchess.player_info.application.game.query.out.model

import java.time.LocalDateTime
import java.util.UUID

data class GameSnippetView(
    val id: UUID,
    val whiteUsername: String,
    val whiteUserPointChange: Int,
    val blackUsername: String,
    val blackUserPointChange: Int,
    val gameResult: String,
    val endTime: LocalDateTime
)
