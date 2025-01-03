package pl.illchess.stockfish.adapter.board.command.out.okhttp.dto

import java.util.UUID

data class ResignGameRequest(
    val boardId: UUID,
    val username: String
)
