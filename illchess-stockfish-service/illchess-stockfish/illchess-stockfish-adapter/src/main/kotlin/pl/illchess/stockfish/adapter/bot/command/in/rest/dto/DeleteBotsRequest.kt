package pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto

import pl.illchess.stockfish.application.bot.command.`in`.DeleteBotsUseCase.DeleteBotsCmd

data class DeleteBotsRequest(
    val usernames: List<String>
) {
    fun toCmd() = DeleteBotsCmd(usernames)
}