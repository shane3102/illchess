package pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto

import pl.illchess.stockfish.application.bot.command.`in`.DeleteBotsUseCase.DeleteBotsCmd

data class DeleteBotRequest(
    val username: String
) {
    fun toCmd() = DeleteBotsCmd(listOf(username))
}