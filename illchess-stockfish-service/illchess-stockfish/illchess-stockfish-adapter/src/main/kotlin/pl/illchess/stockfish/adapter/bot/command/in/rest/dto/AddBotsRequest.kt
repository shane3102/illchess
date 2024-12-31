package pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto

import pl.illchess.stockfish.application.bot.command.`in`.AddBotsUseCase.AddBotsCmd
import pl.illchess.stockfish.application.bot.command.`in`.AddBotsUseCase.AddBotsCmd.AddedBotCmd

data class AddBotsRequest(
    val usernames: List<String>
) {
    fun toCmd() = AddBotsCmd(
        usernames.map {
            AddedBotCmd(it, 5, 1)
        }
    )
}