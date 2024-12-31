package pl.illchess.stockfish.application.bot.command.`in`

import pl.illchess.stockfish.domain.bot.command.DeleteBots
import pl.illchess.stockfish.domain.bot.domain.Username

interface DeleteBotsUseCase {

    fun deleteBots(cmd: DeleteBotsCmd)

    data class DeleteBotsCmd(
        val deletedBotsUsernames: List<String>
    ) {
        fun toCommand() = DeleteBots(
           deletedBotsUsernames.map { Username(it) }
        )
    }
}