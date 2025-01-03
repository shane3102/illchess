package pl.illchess.stockfish.application.bot.command.`in`

import pl.illchess.stockfish.domain.bot.command.AddBots
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username

interface AddBotsUseCase {

    fun addBots(cmd: AddBotsCmd)

    data class AddBotsCmd(
        val addedBotCmd: List<AddedBotCmd>
    ) {

        fun toCommand() = AddBots(
           addedBotCmd.map {
               Bot(
                   Username(it.username),
                   null,
                   it.obtainedBestMovesCount,
                   it.searchedDepth
               )
           }
        )

        data class AddedBotCmd(
            val username: String,
            val obtainedBestMovesCount: Int,
            val searchedDepth: Int
        )
    }

}