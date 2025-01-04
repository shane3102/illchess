package pl.illchess.stockfish.application.bot.query.out

import pl.illchess.stockfish.application.bot.query.out.model.BotView

interface BotViewQueryPort {
    fun listAllCurrentlyRunBots(): List<BotView>

    fun getMaxBotCount(): Int
}