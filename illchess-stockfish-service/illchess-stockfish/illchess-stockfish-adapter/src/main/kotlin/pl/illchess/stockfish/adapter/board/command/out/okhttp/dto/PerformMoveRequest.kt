package pl.illchess.stockfish.adapter.board.command.out.okhttp.dto

import java.util.UUID

data class PerformMoveRequest(
    val boardId: UUID,
    val startSquare: String,
    val targetSquare: String,
    val pawnPromotedToPieceType: String?,
    val username: String
)
