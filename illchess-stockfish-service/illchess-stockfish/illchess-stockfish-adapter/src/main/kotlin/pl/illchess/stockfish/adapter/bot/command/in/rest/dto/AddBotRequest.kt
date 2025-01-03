package pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto

import pl.illchess.stockfish.application.bot.command.`in`.AddBotsUseCase.AddBotsCmd

data class AddBotRequest(
    val username: String
) {
    fun toCmd() = AddBotsCmd(
        listOf(AddBotsCmd.AddedBotCmd(username, 5, 1))
    )
}