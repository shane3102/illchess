package pl.illchess.stockfish.adapter.board.command.out.okhttp.dto

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.UUID

@RegisterForReflection
data class PerformMoveRequest(
    val gameId: UUID,
    val startSquare: String,
    val targetSquare: String,
    val pawnPromotedToPieceType: String?,
    val username: String
)
