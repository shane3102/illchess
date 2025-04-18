package pl.illchess.stockfish.adapter.board.command.out.okhttp.dto

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.UUID

@RegisterForReflection
data class QuitNotYetStartedGameRequest(
    val gameId: UUID,
    val username: String
)
