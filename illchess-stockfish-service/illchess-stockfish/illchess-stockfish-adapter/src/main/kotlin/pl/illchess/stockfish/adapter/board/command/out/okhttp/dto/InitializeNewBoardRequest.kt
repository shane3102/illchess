package pl.illchess.stockfish.adapter.board.command.out.okhttp.dto

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class InitializeNewBoardRequest(val username: String)
